package com.example.SmartShop.controller;

import com.example.SmartShop.dto.order.OrderCreateDTO;
import com.example.SmartShop.dto.order.OrderDTO;
import com.example.SmartShop.dto.order.OrderUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.exception.AccessDeniedException;
import com.example.SmartShop.service.OrderService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Gestion des commandes")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Accès refusé");
        }
    }

    @Operation(summary = "Créer une commande", description = "Création d'une commande (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande créée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @Valid @RequestBody OrderCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderDTO created = orderService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Récupérer une commande par ID", description = "Retourne les détails d'une commande (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande récupérée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderDTO order = orderService.getById(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Récupérer toutes les commandes", description = "Retourne une liste paginée de commandes (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commandes récupérées avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<OrderDTO> orders = orderService.getAll(pageable);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Mettre à jour une commande", description = "Mise à jour d'une commande existante (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande mise à jour avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable String id,
            @Valid @RequestBody OrderUpdateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderDTO updated = orderService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer une commande", description = "Supprime une commande existante (Admin seulement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Commande supprimée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
