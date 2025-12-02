package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.payment.PaymentCreateDTO;
import com.example.SmartShop.dto.payment.PaymentDTO;
import com.example.SmartShop.dto.payment.PaymentUpdateDTO;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.Payment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T17:19:29+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toEntity(PaymentCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Payment.PaymentBuilder payment = Payment.builder();

        payment.order( paymentCreateDTOToOrder( dto ) );
        payment.amount( dto.getAmount() );
        payment.type( dto.getType() );
        payment.paymentDate( dto.getPaymentDate() );

        return payment.build();
    }

    @Override
    public PaymentDTO toDTO(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentDTO paymentDTO = new PaymentDTO();

        paymentDTO.setOrderId( paymentOrderId( payment ) );
        paymentDTO.setId( payment.getId() );
        paymentDTO.setPaymentNumber( payment.getPaymentNumber() );
        paymentDTO.setAmount( payment.getAmount() );
        paymentDTO.setType( payment.getType() );
        paymentDTO.setStatus( payment.getStatus() );
        paymentDTO.setPaymentDate( payment.getPaymentDate() );
        paymentDTO.setEncashmentDate( payment.getEncashmentDate() );

        return paymentDTO;
    }

    @Override
    public void updateEntityFromDTO(PaymentUpdateDTO dto, Payment entity) {
        if ( dto == null ) {
            return;
        }

        entity.setStatus( dto.getStatus() );
        entity.setEncashmentDate( dto.getEncashmentDate() );
    }

    protected Order paymentCreateDTOToOrder(PaymentCreateDTO paymentCreateDTO) {
        if ( paymentCreateDTO == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.id( paymentCreateDTO.getOrderId() );

        return order.build();
    }

    private String paymentOrderId(Payment payment) {
        if ( payment == null ) {
            return null;
        }
        Order order = payment.getOrder();
        if ( order == null ) {
            return null;
        }
        String id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
