package com.example.SmartShop.entity;

import com.example.SmartShop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    private String id;

    // Le client (Many orders -> one client)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal subTotal = BigDecimal.ZERO; // somme prix HT * qty

    @Column(precision = 19, scale = 2)
    private BigDecimal totalDiscount = BigDecimal.ZERO; // remises cumulatives

    @Column(precision = 19, scale = 2)
    private BigDecimal netHt = BigDecimal.ZERO; // HT après remise

    @Column(precision = 19, scale = 2)
    private BigDecimal vat = BigDecimal.ZERO; // TVA (configurable)

    @Column(precision = 19, scale = 2)
    private BigDecimal totalTtc = BigDecimal.ZERO;

    private String promoCode; // optional, format PROMO-XXXX

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amountRemaining = BigDecimal.ZERO; // à payer

    // helper
    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
    }
}
