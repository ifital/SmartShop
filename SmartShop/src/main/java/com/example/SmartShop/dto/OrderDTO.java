package com.example.SmartShop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long clientId;
    private String clientName;

    private LocalDateTime orderDate;

    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal tva;
    private BigDecimal total;
    private BigDecimal remainingAmount;

    private String promoCode;
    private String status; // PENDING / CONFIRMED / REJECTED / CANCELED

    private List<OrderItemDTO> items;
    private List<PaymentDTO> payments;
}