package com.example.SmartShop.dto.order;

import com.example.SmartShop.dto.orderItem.OrderItemDTO;
import com.example.SmartShop.entity.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private String id;
    private String clientId;

    private LocalDateTime createdAt;

    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal netHt;
    private BigDecimal vat;
    private BigDecimal totalTtc;

    private String promoCode;
    private OrderStatus status;

    private BigDecimal amountRemaining;

    // liste des items de la commande
    private List<OrderItemDTO> items;
}
