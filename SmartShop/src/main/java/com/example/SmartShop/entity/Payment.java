package com.example.SmartShop.entity;

import com.example.SmartShop.entity.enums.PaymentStatus;
import com.example.SmartShop.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // numéro séquentiel de paiement pour la commande (1,2,3...)
    @Column(nullable = false)
    private Integer paymentNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.EN_ATTENTE;

    private String reference; // reçu, chq num, virement ref

    private String bankName; // si applicable

    private LocalDateTime paymentDate; // date fournie par client

    private LocalDateTime encashmentDate; // date d'encaissement si applicable
}
