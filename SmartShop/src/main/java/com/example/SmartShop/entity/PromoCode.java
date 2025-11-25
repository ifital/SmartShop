package com.example.SmartShop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    @Column(nullable = false, unique = true)
    private String code;

    // remise en pourcentage (ex: 5 -> 5%)
    @Column(nullable = false)
    private Integer percent;

    // usage unique ou multiple
    @Column(nullable = false)
    private Boolean singleUse = true;

    private Boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt;
}
