package com.example.SmartShop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice; // HT

    @PositiveOrZero
    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private boolean deleted = false;

}
