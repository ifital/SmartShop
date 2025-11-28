package com.example.SmartShop.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;

@Data
public class ProductCreateDTO {

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal unitPrice;

    @PositiveOrZero
    private Integer stock;
}
