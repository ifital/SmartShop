package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.OrderItemCreateDTO;
import com.example.SmartShop.dto.OrderItemDTO;
import com.example.SmartShop.dto.OrderItemUpdateDTO;
import com.example.SmartShop.entity.OrderItem;
import com.example.SmartShop.entity.Product;
import com.example.SmartShop.mapper.OrderItemMapper;
import com.example.SmartShop.repository.OrderItemRepository;
import com.example.SmartShop.repository.ProductRepository;
import com.example.SmartShop.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemDTO create(OrderItemCreateDTO dto) {

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        OrderItem item = orderItemMapper.toEntity(dto);

        item.setProduct(product);
        item.setUnitPrice(product.getUnitPrice());
        item.setLineTotal(
                product.getUnitPrice()
                        .multiply(BigDecimal.valueOf(dto.getQuantity()))
        );

        OrderItem saved = orderItemRepository.save(item);
        return orderItemMapper.toDTO(saved);
    }

    @Override
    public OrderItemDTO update(String id, OrderItemUpdateDTO dto) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem introuvable"));

        orderItemMapper.updateEntityFromDTO(dto, item);

        // recalcul du total
        item.setLineTotal(
                item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
        );

        OrderItem updated = orderItemRepository.save(item);
        return orderItemMapper.toDTO(updated);
    }

    @Override
    public OrderItemDTO getById(String id) {
        return orderItemRepository.findById(id)
                .map(orderItemMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("OrderItem introuvable"));
    }

    @Override
    public void delete(String id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem introuvable"));

        orderItemRepository.delete(item);
    }

    @Override
    public Page<OrderItemDTO> getAll(Pageable pageable) {
        return orderItemRepository.findAll(pageable)
                .map(orderItemMapper::toDTO);
    }

    @Override
    public Page<OrderItemDTO> getByOrderId(String orderId, Pageable pageable) {

        List<OrderItemDTO> items = orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), items.size());
        int end = Math.min(start + pageable.getPageSize(), items.size());

        List<OrderItemDTO> subList = items.subList(start, end);

        return new PageImpl<>(subList, pageable, items.size());
    }
}
