package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.PaymentDTO;
import com.example.SmartShop.entity.Payment;
import com.example.SmartShop.entity.enums.PaymentStatus;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import javax.annotation.processing.Generated;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T17:27:45+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    private final DatatypeFactory datatypeFactory;

    public PaymentMapperImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch ( DatatypeConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

    @Override
    public PaymentDTO toDto(Payment entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentDTO paymentDTO = new PaymentDTO();

        if ( entity.getId() != null ) {
            paymentDTO.setId( Long.parseLong( entity.getId() ) );
        }
        if ( entity.getPaymentNumber() != null ) {
            paymentDTO.setPaymentNumber( entity.getPaymentNumber() );
        }
        paymentDTO.setAmount( entity.getAmount() );
        if ( entity.getStatus() != null ) {
            paymentDTO.setStatus( entity.getStatus().name() );
        }
        paymentDTO.setPaymentDate( xmlGregorianCalendarToLocalDate( localDateTimeToXmlGregorianCalendar( entity.getPaymentDate() ) ) );
        paymentDTO.setEncashmentDate( xmlGregorianCalendarToLocalDate( localDateTimeToXmlGregorianCalendar( entity.getEncashmentDate() ) ) );

        return paymentDTO;
    }

    @Override
    public Payment toEntity(PaymentDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Payment.PaymentBuilder payment = Payment.builder();

        if ( dto.getId() != null ) {
            payment.id( String.valueOf( dto.getId() ) );
        }
        payment.paymentNumber( dto.getPaymentNumber() );
        payment.amount( dto.getAmount() );
        if ( dto.getStatus() != null ) {
            payment.status( Enum.valueOf( PaymentStatus.class, dto.getStatus() ) );
        }
        payment.paymentDate( xmlGregorianCalendarToLocalDateTime( localDateToXmlGregorianCalendar( dto.getPaymentDate() ) ) );
        payment.encashmentDate( xmlGregorianCalendarToLocalDateTime( localDateToXmlGregorianCalendar( dto.getEncashmentDate() ) ) );

        return payment.build();
    }

    private XMLGregorianCalendar localDateToXmlGregorianCalendar( LocalDate localDate ) {
        if ( localDate == null ) {
            return null;
        }

        return datatypeFactory.newXMLGregorianCalendarDate(
            localDate.getYear(),
            localDate.getMonthValue(),
            localDate.getDayOfMonth(),
            DatatypeConstants.FIELD_UNDEFINED );
    }

    private XMLGregorianCalendar localDateTimeToXmlGregorianCalendar( LocalDateTime localDateTime ) {
        if ( localDateTime == null ) {
            return null;
        }

        return datatypeFactory.newXMLGregorianCalendar(
            localDateTime.getYear(),
            localDateTime.getMonthValue(),
            localDateTime.getDayOfMonth(),
            localDateTime.getHour(),
            localDateTime.getMinute(),
            localDateTime.getSecond(),
            localDateTime.get( ChronoField.MILLI_OF_SECOND ),
            DatatypeConstants.FIELD_UNDEFINED );
    }

    private static LocalDate xmlGregorianCalendarToLocalDate( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        return LocalDate.of( xcal.getYear(), xcal.getMonth(), xcal.getDay() );
    }

    private static LocalDateTime xmlGregorianCalendarToLocalDateTime( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        if ( xcal.getYear() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMonth() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getDay() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getHour() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMinute() != DatatypeConstants.FIELD_UNDEFINED
        ) {
            if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED
                && xcal.getMillisecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond(),
                    Duration.ofMillis( xcal.getMillisecond() ).getNano()
                );
            }
            else if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond()
                );
            }
            else {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute()
                );
            }
        }
        return null;
    }
}
