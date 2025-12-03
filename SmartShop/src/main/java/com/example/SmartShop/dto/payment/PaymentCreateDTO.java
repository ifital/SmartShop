package com.example.SmartShop.dto.payment;

import com.example.SmartShop.entity.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentCreateDTO {

    @NotNull
    private String orderId;

    @Positive
    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentType type;

    private LocalDateTime paymentDate; // optionnel, fournie par client

    private String chequeNumber;

    private String bankName;

    private LocalDateTime dueDate;

    private String transferReference;
}
