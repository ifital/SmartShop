package com.example.SmartShop.service;

import com.example.SmartShop.dto.OrderItemCreateDTO;
import com.example.SmartShop.dto.OrderItemDTO;
import com.example.SmartShop.dto.OrderItemUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {

    OrderItemDTO create(OrderItemCreateDTO dto);

    OrderItemDTO update(String id, OrderItemUpdateDTO dto);

    OrderItemDTO getById(String id);

    void delete(String id);

    Page<OrderItemDTO> getAll(Pageable pageable);

    Page<OrderItemDTO> getByOrderId(String orderId, Pageable pageable);
}
