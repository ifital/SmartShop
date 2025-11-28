package com.example.SmartShop.mapper;


import com.example.SmartShop.dto.payment.PaymentCreateDTO;
import com.example.SmartShop.dto.payment.PaymentDTO;
import com.example.SmartShop.dto.payment.PaymentUpdateDTO;
import com.example.SmartShop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "orderId", target = "order.id")
    Payment toEntity(PaymentCreateDTO dto);

    @Mapping(source = "order.id", target = "orderId")
    PaymentDTO toDTO(Payment payment);

    void updateEntityFromDTO(PaymentUpdateDTO dto, @MappingTarget Payment entity);
}
