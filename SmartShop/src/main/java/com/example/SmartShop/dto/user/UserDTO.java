package com.example.SmartShop.dto.user;

import com.example.SmartShop.entity.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {

    private String id;
    private String username;
    private UserRole role; // CLIENT _ ADMIN
}