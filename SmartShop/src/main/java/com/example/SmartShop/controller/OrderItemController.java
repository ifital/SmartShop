package com.example.SmartShop.controller;

import com.example.SmartShop.dto.OrderItemCreateDTO;
import com.example.SmartShop.dto.OrderItemDTO;
import com.example.SmartShop.dto.OrderItemUpdateDTO;
import com.example.SmartShop.dto.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.OrderItemService;
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
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;
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
    public ResponseEntity<OrderItemDTO> createOrderItem(
            @Valid @RequestBody OrderItemCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderItemDTO created = orderItemService.create(dto);
        return ResponseEntity.ok(created);
    }

    // -------------------------------
    // GET BY ID (ADMIN ONLY)
    // -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItem(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderItemDTO item = orderItemService.getById(id);
        return ResponseEntity.ok(item);
    }

    // -------------------------------
    // GET ALL (ADMIN ONLY, paginated)
    // -------------------------------
    @GetMapping
    public ResponseEntity<Page<OrderItemDTO>> getAllOrderItems(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<OrderItemDTO> items = orderItemService.getAll(pageable);
        return ResponseEntity.ok(items);
    }

    // -------------------------------
    // GET BY ORDER ID (ADMIN ONLY, paginated)
    // -------------------------------
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

    // -------------------------------
    // UPDATE (ADMIN ONLY)
    // -------------------------------
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

    // -------------------------------
    // DELETE (ADMIN ONLY)
    // -------------------------------
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
