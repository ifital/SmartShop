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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    Payment toEntity(PaymentCreateDTO dto);

    @Mapping(source = "order.id", target = "orderId")
    PaymentDTO toDTO(Payment payment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentNumber", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    void updateEntityFromDTO(PaymentUpdateDTO dto, @MappingTarget Payment entity);
}
