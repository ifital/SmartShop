package com.example.SmartShop.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {

    private String clientId;

    // Items de la commande
    private List<OrderItemCreateDTO> items;

    private String promoCode;
}
