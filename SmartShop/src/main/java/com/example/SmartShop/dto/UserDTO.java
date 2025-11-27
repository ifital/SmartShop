package com.example.SmartShop.dto;

import com.example.SmartShop.entity.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {

    private String id;
    private String username;
    private UserRole role; // CLIENT _ ADMIN
}