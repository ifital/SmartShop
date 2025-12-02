package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.order.OrderCreateDTO;
import com.example.SmartShop.dto.order.OrderDTO;
import com.example.SmartShop.dto.order.OrderUpdateDTO;
import com.example.SmartShop.dto.orderItem.OrderItemCreateDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.OrderItem;
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

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    // Service de fidélité
    private final LoyaltyService loyaltyService;

    // TVA Fixe
    private static final BigDecimal TVA_RATE = new BigDecimal("0.20"); // 20%

    // ------------------------------------------------------
    //                      CREATE ORDER
    // ------------------------------------------------------
    @Override
    public OrderDTO create(OrderCreateDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        Order order = orderMapper.toEntity(dto);
        order.setClient(client);

        // Ajouter les items
        dto.getItems().forEach(itemDTO -> addItemToOrder(order, itemDTO));

        // Calcul complet des totaux avec remise fidélité
        calculateTotals(order);

        Order saved = orderRepository.save(order);

        return orderMapper.toDTO(saved);
    }

    // ------------------------------------------------------
    //                      UPDATE ORDER
    // ------------------------------------------------------
    @Override
    public OrderDTO update(String id, OrderUpdateDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        orderMapper.updateEntityFromDTO(dto, order);

        // Recalculer les totaux (promo, fidélité…)
        calculateTotals(order);

        // Si commande confirmée → mise à jour du Tier du client
        if (dto.getStatus() == OrderStatus.CONFIRMED) {
            loyaltyService.updateClientTier(order.getClient());
            clientRepository.save(order.getClient());
        }

        Order updated = orderRepository.save(order);

        return orderMapper.toDTO(updated);
    }

    // ------------------------------------------------------
    //                      GET BY ID
    // ------------------------------------------------------
    @Override
    public OrderDTO getById(String id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    // ------------------------------------------------------
    //                      GET ALL
    // ------------------------------------------------------
    @Override
    public Page<OrderDTO> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    // ------------------------------------------------------
    //                      DELETE ORDER
    // ------------------------------------------------------
    @Override
    public void delete(String id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Commande introuvable");
        }
        orderRepository.deleteById(id);
    }

    // ------------------------------------------------------
    //                GESTION DES ORDER ITEMS
    // ------------------------------------------------------

    private void addItemToOrder(Order order, OrderItemCreateDTO itemDTO) {

        var product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        OrderItem item = orderItemMapper.toEntity(itemDTO);
        item.setProduct(product);

        item.setUnitPrice(product.getUnitPrice());
        item.setLineTotal(product.getUnitPrice()
                .multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

        order.addItem(item);
    }

    // ------------------------------------------------------
    //                  CALCUL DES TOTAUX
    // ------------------------------------------------------

    private void calculateTotals(Order order) {

        Client client = order.getClient();

        // 1) Subtotal = somme (unitPrice * qty)
        BigDecimal subTotal = order.getItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setSubTotal(subTotal);

        // ------------------- REMISE FIDÉLITÉ -------------------
        BigDecimal loyaltyDiscount = loyaltyService.applyTierDiscount(client, subTotal);

        // ------------------- REMISE PROMO ----------------------
        BigDecimal promoDiscount = BigDecimal.ZERO;
        if (order.getPromoCode() != null && !order.getPromoCode().isEmpty()) {
            promoDiscount = subTotal.multiply(new BigDecimal("0.05")); // promo 5%
        }

        // Remise totale
        BigDecimal totalDiscount = loyaltyDiscount.add(promoDiscount);
        order.setTotalDiscount(totalDiscount);

        // Net HT = Subtotal - Discounts
        BigDecimal netHt = subTotal.subtract(totalDiscount);
        order.setNetHt(netHt);

        // TVA = netHt * TVA_RATE
        BigDecimal vat = netHt.multiply(TVA_RATE);
        order.setVat(vat);

        // Total TTC
        BigDecimal totalTtc = netHt.add(vat);
        order.setTotalTtc(totalTtc);

        // Reste à payer
        order.setAmountRemaining(totalTtc);
    }
}
