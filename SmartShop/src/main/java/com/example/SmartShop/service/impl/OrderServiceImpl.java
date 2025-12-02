package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.order.OrderCreateDTO;
import com.example.SmartShop.dto.order.OrderDTO;
import com.example.SmartShop.dto.order.OrderUpdateDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.OrderItem;
import com.example.SmartShop.entity.Product;
import com.example.SmartShop.entity.enums.OrderStatus;
import com.example.SmartShop.mapper.OrderItemMapper;
import com.example.SmartShop.mapper.OrderMapper;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.repository.ProductRepository;
import com.example.SmartShop.service.LoyaltyService;
import com.example.SmartShop.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final LoyaltyService loyaltyService;

    private static final BigDecimal TVA_RATE = new BigDecimal("0.20"); // 20%

    // -------------------------------------------------------------------------
    //                                CREATE ORDER
    // -------------------------------------------------------------------------
    @Override
    public OrderDTO create(OrderCreateDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        Order order = orderMapper.toEntity(dto);
        order.setClient(client);

        // Convert DTOs → OrderItems + validation stock
        List<OrderItem> orderItems = dto.getItems().stream()
                .map(itemDTO -> {
                    Product product = productRepository.findById(itemDTO.getProductId())
                            .orElseThrow(() -> new RuntimeException("Produit introuvable"));

                    if (product.getStock() < itemDTO.getQuantity()) {
                        throw new RuntimeException(
                                "Stock insuffisant pour le produit : " + product.getName()
                        );
                    }

                    OrderItem item = orderItemMapper.toEntity(itemDTO);
                    item.setProduct(product);
                    item.setUnitPrice(product.getUnitPrice());
                    item.setLineTotal(product.getUnitPrice()
                            .multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

                    return item;
                })
                .toList();

        orderItems.forEach(order::addItem);

        // Calculs (HT, TVA, TTC, remises…)
        calculateTotals(order);

        Order saved = orderRepository.save(order);
        return orderMapper.toDTO(saved);
    }

    // -------------------------------------------------------------------------
    //                                UPDATE ORDER
    // -------------------------------------------------------------------------
    @Override
    public OrderDTO update(String id, OrderUpdateDTO dto) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        orderMapper.updateEntityFromDTO(dto, order);

        // Recalcul des totaux si changement (promo, discount…)
        calculateTotals(order);

        // Gestion logique des statuts
        switch (dto.getStatus()) {
            case CONFIRMED -> confirmOrder(order);
            case CANCELED -> cancelOrder(order);
            default -> order.setStatus(dto.getStatus());
        }

        Order updated = orderRepository.save(order);
        return orderMapper.toDTO(updated);
    }

    // -------------------------------------------------------------------------
    //                                READ
    // -------------------------------------------------------------------------
    @Override
    public OrderDTO getById(String id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    @Override
    public Page<OrderDTO> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    // -------------------------------------------------------------------------
    //                                DELETE
    // -------------------------------------------------------------------------
    @Override
    public void delete(String id) {
        if (!orderRepository.existsById(id))
            throw new RuntimeException("Commande introuvable");

        orderRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    //                        PRIVATE LOGIC : CALCULS
    // -------------------------------------------------------------------------
    private void calculateTotals(Order order) {

        BigDecimal subTotal = order.getItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setSubTotal(subTotal);

        // Remise fidélité
        BigDecimal loyaltyDiscount =
                loyaltyService.applyTierDiscount(order.getClient(), subTotal);

        // Remise promo
        BigDecimal promoDiscount = BigDecimal.ZERO;
        if (order.getPromoCode() != null
                && order.getPromoCode().matches("PROMO-\\d{4}")) {
            promoDiscount = subTotal.multiply(new BigDecimal("0.05"));
        }

        BigDecimal totalDiscount = loyaltyDiscount.add(promoDiscount);
        order.setTotalDiscount(totalDiscount);

        BigDecimal netHt = subTotal.subtract(totalDiscount);
        order.setNetHt(netHt);

        BigDecimal vat = netHt.multiply(TVA_RATE);
        order.setVat(vat);

        BigDecimal totalTtc = netHt.add(vat);
        order.setTotalTtc(totalTtc);

        order.setAmountRemaining(totalTtc);
    }

    // -------------------------------------------------------------------------
    //                        PRIVATE LOGIC : STATUTS
    // -------------------------------------------------------------------------
    private void confirmOrder(Order order) {

        // Vérifier stock
        boolean insufficientStock = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getStock() < item.getQuantity());

        if (insufficientStock) {
            order.setStatus(OrderStatus.REJECTED);
            return;
        }

        // Décrémenter les stocks
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        });

        // Mise à jour fidélité
        loyaltyService.updateClientTier(order.getClient());
        clientRepository.save(order.getClient());

        order.setStatus(OrderStatus.CONFIRMED);
    }

    private void cancelOrder(Order order) {
        order.setStatus(OrderStatus.CANCELED);
        // (Optionnel : remonter stock si nécessaire)
    }
}
