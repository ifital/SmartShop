package com.example.SmartShop.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
