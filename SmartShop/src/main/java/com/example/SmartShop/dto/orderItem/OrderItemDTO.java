package com.example.SmartShop.dto.orderItem;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private String id;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private String orderId;
}