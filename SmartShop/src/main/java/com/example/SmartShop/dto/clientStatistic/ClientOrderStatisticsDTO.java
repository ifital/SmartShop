package com.example.SmartShop.dto.clientStatistic;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ClientOrderStatisticsDTO {

    private Long totalOrders;              // nombre total de commandes du client
    private BigDecimal totalAmount;        // montant TTC cumulé des commandes CONFIRMED
    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;
}
