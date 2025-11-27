package com.example.SmartShop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClientDTO {
    private String id;
    private String name;
    private String email;
    private String tier; // BASIC / SILVER / GOLD / PLATINUM

}