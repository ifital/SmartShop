package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.PaymentDTO;
import com.example.SmartShop.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toDto(Payment entity);
    Payment toEntity(PaymentDTO dto);
}
