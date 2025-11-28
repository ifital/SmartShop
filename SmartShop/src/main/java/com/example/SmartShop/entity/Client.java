package com.example.SmartShop.entity;

import com.example.SmartShop.entity.enums.CustomerTier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @NotBlank
    @Column(nullable = false)
    private String name; // nom entreprise/contact

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerTier tier = CustomerTier.BASIC; // BASIC, SILVER, GOLD, PLATINUM

}
