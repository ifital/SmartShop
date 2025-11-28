package com.example.SmartShop.dto.product;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private String id;
    private String name;
    private BigDecimal unitPrice;
    private Integer stock;
    private boolean deleted ;
}