package com.example.SmartShop.service.impl;


import com.example.SmartShop.dto.orderStatistic.ClientOrderHistoryDTO;
import com.example.SmartShop.dto.orderStatistic.ClientOrderStatisticsDTO;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.service.ClientStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientStatisticsServiceImpl implements ClientStatisticsService {

    private final OrderRepository orderRepository;

    @Override
    public ClientOrderStatisticsDTO getClientStatistics(String clientId) {

        Long totalOrders = orderRepository.countOrdersByClient(clientId);
        var totalAmount = orderRepository.sumConfirmedOrdersAmount(clientId);
        var first = orderRepository.getFirstOrderDate(clientId);
        var last = orderRepository.getLastOrderDate(clientId);

        return ClientOrderStatisticsDTO.builder()
                .totalOrders(totalOrders)
                .totalAmount(totalAmount)
                .firstOrderDate(first)
                .lastOrderDate(last)
                .build();
    }

    @Override
    public List<ClientOrderHistoryDTO> getClientOrderHistory(String clientId) {

        List<Order> orders = orderRepository.findByClientIdOrderByCreatedAtDesc(clientId);

        return orders.stream()
                .map(o -> ClientOrderHistoryDTO.builder()
                        .orderId(o.getId())
                        .createdAt(o.getCreatedAt())
                        .totalTtc(o.getTotalTtc())
                        .status(o.getStatus())
                        .build())
                .toList();
    }
}
