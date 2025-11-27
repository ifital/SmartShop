package com.example.SmartShop.dto;

import com.example.SmartShop.entity.enums.CustomerTier;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientUpdateDTO {

    private String name;

    private String email;

    private CustomerTier tier;
}
