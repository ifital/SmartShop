package com.example.SmartShop.dto.clientStatistic;

import com.example.SmartShop.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ClientOrderHistoryDTO {
    private String orderId;
    private LocalDateTime createdAt;
    private BigDecimal totalTtc;
    private OrderStatus status;
}
