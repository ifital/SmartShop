package com.example.SmartShop.controller;

import com.example.SmartShop.dto.orderItem.OrderItemCreateDTO;
import com.example.SmartShop.dto.orderItem.OrderItemDTO;
import com.example.SmartShop.dto.orderItem.OrderItemUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.OrderItemService;
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
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@Tag(name = "Order Items", description = "Gestion des items de commande (CRUD)")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final UserService userService;

    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    @Operation(summary = "Créer un item de commande", description = "Ajoute un item à une commande existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(
            @Valid @RequestBody OrderItemCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderItemDTO created = orderItemService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Récupérer un item de commande par ID", description = "Retourne les détails d'un item spécifique")
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItem(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderItemDTO item = orderItemService.getById(id);
        return ResponseEntity.ok(item);
    }

    @Operation(summary = "Lister tous les items de commande", description = "Retourne tous les items paginés")
    @GetMapping
    public ResponseEntity<Page<OrderItemDTO>> getAllOrderItems(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<OrderItemDTO> items = orderItemService.getAll(pageable);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Lister les items d'une commande", description = "Retourne les items pour une commande spécifique (paginé)")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<OrderItemDTO>> getItemsByOrder(
            @PathVariable String orderId,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<OrderItemDTO> items = orderItemService.getByOrderId(orderId, pageable);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Mettre à jour un item de commande", description = "Modifie un item existant")
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable String id,
            @Valid @RequestBody OrderItemUpdateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderItemDTO updated = orderItemService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer un item de commande", description = "Supprime un item spécifique d'une commande")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
