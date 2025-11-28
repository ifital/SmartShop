package com.example.SmartShop.controller;


import com.example.SmartShop.dto.payment.PaymentCreateDTO;
import com.example.SmartShop.dto.payment.PaymentDTO;
import com.example.SmartShop.dto.payment.PaymentUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.PaymentService;
import com.example.SmartShop.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    // -------------------------------
    // Vérification ADMIN via HttpSession
    // -------------------------------
    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    // -------------------------------
    // CREATE (ADMIN ONLY)
    // -------------------------------
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(
            @Valid @RequestBody PaymentCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        PaymentDTO created = paymentService.create(dto);
        return ResponseEntity.ok(created);
    }

    // -------------------------------
    // GET BY ID (ADMIN ONLY)
    // -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPayment(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        PaymentDTO payment = paymentService.getById(id);
        return ResponseEntity.ok(payment);
    }

    // -------------------------------
    // GET ALL (ADMIN ONLY, paginated)
    // -------------------------------
    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<PaymentDTO> payments = paymentService.getAll(pageable);
        return ResponseEntity.ok(payments);
    }

    // -------------------------------
    // GET BY ORDER ID (ADMIN ONLY, paginated)
    // -------------------------------
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<PaymentDTO>> getPaymentsByOrder(
            @PathVariable String orderId,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<PaymentDTO> payments = paymentService.getByOrderId(orderId, pageable);
        return ResponseEntity.ok(payments);
    }

    // -------------------------------
    // UPDATE (ADMIN ONLY)
    // -------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(
            @PathVariable String id,
            @Valid @RequestBody PaymentUpdateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        PaymentDTO updated = paymentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // -------------------------------
    // DELETE (ADMIN ONLY)
    // -------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
