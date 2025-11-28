package com.example.SmartShop.dto.orderItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemCreateDTO {

    @NotBlank
    private String productId;

    @NotNull
    @Positive
    private Integer quantity;

    private String orderId; // optionnel si on crée l’item avec l’order existant
}
