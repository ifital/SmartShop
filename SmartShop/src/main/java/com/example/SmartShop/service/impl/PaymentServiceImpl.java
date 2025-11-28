package com.example.SmartShop.service.impl;


import com.example.SmartShop.dto.payment.PaymentCreateDTO;
import com.example.SmartShop.dto.payment.PaymentDTO;
import com.example.SmartShop.dto.payment.PaymentUpdateDTO;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.Payment;
import com.example.SmartShop.mapper.PaymentMapper;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.repository.PaymentRepository;
import com.example.SmartShop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDTO create(PaymentCreateDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        Payment payment = paymentMapper.toEntity(dto);
        payment.setOrder(order);

        // Déterminer le numéro de paiement (séquentiel)
        int nextNumber = (int) paymentRepository.findByOrderId(order.getId()).size() + 1;
        payment.setPaymentNumber(nextNumber);

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDTO(saved);
    }

    @Override
    public PaymentDTO update(String id, PaymentUpdateDTO dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        paymentMapper.updateEntityFromDTO(dto, payment);

        Payment updated = paymentRepository.save(payment);
        return paymentMapper.toDTO(updated);
    }

    @Override
    public PaymentDTO getById(String id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
    }

    @Override
    public Page<PaymentDTO> getAll(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(paymentMapper::toDTO);
    }

    @Override
    public Page<PaymentDTO> getByOrderId(String orderId, Pageable pageable) {
        List<PaymentDTO> payments = paymentRepository.findByOrderId(orderId)
                .stream()
                .map(paymentMapper::toDTO)
                .toList();

        int start = Math.min((int) pageable.getOffset(), payments.size());
        int end = Math.min(start + pageable.getPageSize(), payments.size());

        List<PaymentDTO> subList = payments.subList(start, end);
        return new PageImpl<>(subList, pageable, payments.size());
    }

    @Override
    public void delete(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Paiement introuvable");
        }
        paymentRepository.deleteById(id);
    }
}
