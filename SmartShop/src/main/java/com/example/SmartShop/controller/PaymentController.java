package com.example.SmartShop.controller;

import com.example.SmartShop.dto.payment.PaymentCreateDTO;
import com.example.SmartShop.dto.payment.PaymentDTO;
import com.example.SmartShop.dto.payment.PaymentUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.exception.AccessDeniedException;
import com.example.SmartShop.service.PaymentService;
import com.example.SmartShop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payments", description = "Gestion des paiements")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    @Operation(summary = "Créer un paiement", description = "Création d'un paiement pour une commande (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement créé avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(
            @Valid @RequestBody PaymentCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        PaymentDTO created = paymentService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Récupérer un paiement par ID", description = "Retourne les détails d'un paiement (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement récupéré avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPayment(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        PaymentDTO payment = paymentService.getById(id);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Récupérer tous les paiements", description = "Retourne une liste paginée de paiements (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiements récupérés avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<PaymentDTO> payments = paymentService.getAll(pageable);
        return ResponseEntity.ok(payments);
    }

    @Operation(summary = "Récupérer les paiements d'une commande", description = "Retourne une liste paginée de paiements pour une commande donnée (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiements récupérés avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
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

    @Operation(summary = "Mettre à jour un paiement", description = "Met à jour le statut ou la date d'encaissement d'un paiement (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement mis à jour avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
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

    @Operation(summary = "Supprimer un paiement", description = "Supprime un paiement existant (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paiement supprimé avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
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
