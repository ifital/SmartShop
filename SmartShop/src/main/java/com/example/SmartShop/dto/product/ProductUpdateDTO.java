package com.example.SmartShop.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class ProductUpdateDTO {

    private String name;

    private BigDecimal unitPrice;

    @PositiveOrZero
    private Integer stock;
}
