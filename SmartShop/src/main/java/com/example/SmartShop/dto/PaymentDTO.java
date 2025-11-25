package com.example.SmartShop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentDTO {
    private Long id;
    private int paymentNumber;
    private BigDecimal amount;

    private String paymentType; // ESPECES / CHEQUE / VIREMENT
    private String status; // EN_ATTENTE / ENCAISSÉ / REJETÉ

    private String reference;
    private String bank;

    private LocalDate paymentDate;
    private LocalDate encashmentDate;
}