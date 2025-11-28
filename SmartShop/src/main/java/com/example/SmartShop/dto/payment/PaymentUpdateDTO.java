package com.example.SmartShop.dto.payment;

import com.example.SmartShop.entity.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentUpdateDTO {

    private PaymentStatus status; // changer le statut si nécessaire
    private LocalDateTime encashmentDate; // date d'encaissement si applicable
}
