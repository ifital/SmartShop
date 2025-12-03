package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.payment.PaymentCreateDTO;
import com.example.SmartShop.dto.payment.PaymentDTO;
import com.example.SmartShop.dto.payment.PaymentUpdateDTO;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.Payment;
import com.example.SmartShop.entity.enums.OrderStatus;
import com.example.SmartShop.entity.enums.PaymentStatus;
import com.example.SmartShop.exception.OrderNotFoundException;
import com.example.SmartShop.exception.PaymentNotFoundException;
import com.example.SmartShop.mapper.PaymentMapper;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.repository.PaymentRepository;
import com.example.SmartShop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    // ---------------------------------------------------------------------
    // CREATE PAYMENT
    // ---------------------------------------------------------------------
    @Override
    public PaymentDTO create(PaymentCreateDTO dto) {

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Commande introuvable"));

        // Vérification du montant restant
        if (dto.getAmount().compareTo(order.getAmountRemaining()) > 0) {
            throw new IllegalArgumentException("Montant du paiement supérieur au restant dû !");
        }

        Payment payment = paymentMapper.toEntity(dto);
        payment.setOrder(order);

        // Numéro séquentiel
        int nextNumber = paymentRepository.findByOrderId(order.getId()).size() + 1;
        payment.setPaymentNumber(nextNumber);

        // Date par défaut
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        // LOGIQUE PAR TYPE
        switch (dto.getType()) {

            case ESPECES -> {
                if (payment.getAmount().compareTo(new BigDecimal("20000")) > 0) {
                    throw new IllegalArgumentException("Le paiement en espèces ne peut dépasser 20 000 DH");
                }
                payment.setStatus(PaymentStatus.ENCAISSE);
            }

            case CHEQUE -> {
                if (dto.getChequeNumber() == null || dto.getBankName() == null) {
                    throw new IllegalArgumentException("Numéro de chèque et banque obligatoires");
                }
                payment.setStatus(PaymentStatus.EN_ATTENTE);
            }

            case VIREMENT -> {
                if (dto.getTransferReference() == null || dto.getBankName() == null) {
                    throw new IllegalArgumentException("Référence virement et banque obligatoires");
                }
                payment.setStatus(PaymentStatus.ENCAISSE);
            }
        }

        // Mise à jour du restant dû
        BigDecimal newRemaining = order.getAmountRemaining().subtract(payment.getAmount());
        order.setAmountRemaining(newRemaining);

        // Mise à jour du statut de la commande
        updateOrderStatusAfterPayment(order);

        orderRepository.save(order);
        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    // ---------------------------------------------------------------------
    // UPDATE PAYMENT
    // ---------------------------------------------------------------------
    @Override
    public PaymentDTO update(String id, PaymentUpdateDTO dto) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Paiement introuvable"));

        // RÈGLE : un paiement encaissé est verrouillé
        if (payment.getStatus() == PaymentStatus.ENCAISSE &&
                dto.getStatus() != PaymentStatus.ENCAISSE) {

            throw new IllegalArgumentException("Un paiement encaissé ne peut pas être modifié");
        }

        paymentMapper.updateEntityFromDTO(dto, payment);
        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    // ---------------------------------------------------------------------
    // READ
    // ---------------------------------------------------------------------
    @Override
    public PaymentDTO getById(String id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDTO)
                .orElseThrow(() -> new PaymentNotFoundException("Paiement introuvable"));
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

    // ---------------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------------
    @Override
    public void delete(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Paiement introuvable");
        }
        paymentRepository.deleteById(id);
    }

    // ---------------------------------------------------------------------
    // PRIVATE LOGIC
    // ---------------------------------------------------------------------
    private void updateOrderStatusAfterPayment(Order order) {

        BigDecimal remaining = order.getAmountRemaining();

        if (remaining.compareTo(BigDecimal.ZERO) == 0) {
            order.setStatus(OrderStatus.PAID); // ensuite admin peut faire CONFIRMED
        }
        else if (remaining.compareTo(order.getTotalTtc()) < 0) {
            order.setStatus(OrderStatus.PARTIALLY_PAID);
        }
        else {
            order.setStatus(OrderStatus.PENDING);
        }
    }
}
