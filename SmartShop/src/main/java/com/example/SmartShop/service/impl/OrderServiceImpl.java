package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.OrderCreateDTO;
import com.example.SmartShop.dto.OrderDTO;
import com.example.SmartShop.dto.OrderItemCreateDTO;
import com.example.SmartShop.dto.OrderUpdateDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.OrderItem;
import com.example.SmartShop.mapper.OrderItemMapper;
import com.example.SmartShop.mapper.OrderMapper;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.repository.ProductRepository;
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

    // TVA Fixe
    private static final BigDecimal TVA_RATE = new BigDecimal("0.20"); // 20%

    // CREATE ORDER
    @Override
    public OrderDTO create(OrderCreateDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        Order order = orderMapper.toEntity(dto);
        order.setClient(client);

        // 3. Ajouter les OrderItems
        dto.getItems().forEach(itemDTO -> addItemToOrder(order, itemDTO));

        // 4. Recalcul des totaux
        calculateTotals(order);

        Order saved = orderRepository.save(order);

        return orderMapper.toDTO(saved);
    }

    // UPDATE ORDER

    @Override
    public OrderDTO update(String id, OrderUpdateDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        orderMapper.updateEntityFromDTO(dto, order);

        // recalcul après changement promo/status
        calculateTotals(order);

        Order updated = orderRepository.save(order);

        return orderMapper.toDTO(updated);
    }

    // GET BY ID

    @Override
    public OrderDTO getById(String id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    // GET ALL
    @Override
    public Page<OrderDTO> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    // DELETE

    @Override
    public void delete(String id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Commande introuvable");
        }
        orderRepository.deleteById(id);
    }

    // ---------- GESTION DES ITEMS ----------

    private void addItemToOrder(Order order, OrderItemCreateDTO itemDTO) {
        ProductRepository productRepo = productRepository;

        var product = productRepo.findById(itemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        OrderItem item = orderItemMapper.toEntity(itemDTO);
        item.setProduct(product);

        item.setUnitPrice(product.getUnitPrice());
        item.setLineTotal(product.getUnitPrice()
                .multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

        order.addItem(item);
    }

    // ---------- CALCUL DES TOTAUX ----------

    private void calculateTotals(Order order) {

        // Subtotal = somme (unitPrice * qty)
        BigDecimal subTotal = order.getItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setSubTotal(subTotal);

        // Remise
        BigDecimal discount = BigDecimal.ZERO;

        if (order.getPromoCode() != null && !order.getPromoCode().isEmpty()) {
            discount = subTotal.multiply(new BigDecimal("0.05")); // promo 5%
        }

        order.setTotalDiscount(discount);

        // Net HT = Subtotal - Discount
        BigDecimal netHt = subTotal.subtract(discount);
        order.setNetHt(netHt);

        // TVA = netHt * TVA_RATE
        BigDecimal vat = netHt.multiply(TVA_RATE);
        order.setVat(vat);

        // Total TTC
        BigDecimal totalTtc = netHt.add(vat);
        order.setTotalTtc(totalTtc);

        // Reste à payer = Total TTC (si paiements arrivent après)
        order.setAmountRemaining(totalTtc);
    }
}
