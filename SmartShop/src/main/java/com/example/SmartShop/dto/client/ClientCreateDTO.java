package com.example.SmartShop.dto.client;

import com.example.SmartShop.entity.enums.CustomerTier;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCreateDTO {

    private String username;

    private String password;

    private String role;

    private String name;

    private String email;

}
