package com.example.SmartShop.dto.payment;

import com.example.SmartShop.entity.enums.PaymentStatus;
import com.example.SmartShop.entity.enums.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    private String id;
    private Integer paymentNumber;
    private String orderId;
    private BigDecimal amount;
    private PaymentType type;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private LocalDateTime encashmentDate;
    private String chequeNumber;
    private String bankName;
    private LocalDateTime dueDate;
    private String transferReference;
}
