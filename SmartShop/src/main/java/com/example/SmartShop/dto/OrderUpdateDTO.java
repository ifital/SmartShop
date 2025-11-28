package com.example.SmartShop.dto;

import com.example.SmartShop.entity.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderUpdateDTO {

    private String promoCode;

    private OrderStatus status;
}
