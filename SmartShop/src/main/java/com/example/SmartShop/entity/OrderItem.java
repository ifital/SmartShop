package com.example.SmartShop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    private String id;

    // produit snapshot : lien + prix unitaire copié lors de la création
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Positive
    @Column(nullable = false)
    private int quantity;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal unitPrice; // prix HT au moment de commande

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal lineTotal; // unitPrice * quantity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
