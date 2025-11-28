package com.example.SmartShop.service;

import com.example.SmartShop.dto.order.OrderCreateDTO;
import com.example.SmartShop.dto.order.OrderDTO;
import com.example.SmartShop.dto.order.OrderUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDTO create(OrderCreateDTO dto);

    OrderDTO update(String id, OrderUpdateDTO dto);

    OrderDTO getById(String id);

    Page<OrderDTO> getAll(Pageable pageable);

    void delete(String id);
}
