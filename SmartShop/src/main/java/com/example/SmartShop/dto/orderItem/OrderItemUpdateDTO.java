package com.example.SmartShop.dto.orderItem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemUpdateDTO {

    @Positive
    private Integer quantity; // possibilité de mise à jour uniquement de la quantité
}
