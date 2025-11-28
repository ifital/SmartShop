package com.example.SmartShop.service;


import com.example.SmartShop.dto.payment.PaymentCreateDTO;
import com.example.SmartShop.dto.payment.PaymentDTO;
import com.example.SmartShop.dto.payment.PaymentUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentDTO create(PaymentCreateDTO dto);

    PaymentDTO update(String id, PaymentUpdateDTO dto);

    PaymentDTO getById(String id);

    Page<PaymentDTO> getAll(Pageable pageable);

    Page<PaymentDTO> getByOrderId(String orderId, Pageable pageable);

    void delete(String id);
}
