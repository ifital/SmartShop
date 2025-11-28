package com.example.SmartShop.dto.client;

import lombok.Data;

@Data
public class ClientDTO {
    private String id;
    private String name;
    private String email;
    private String tier; // BASIC / SILVER / GOLD / PLATINUM

}