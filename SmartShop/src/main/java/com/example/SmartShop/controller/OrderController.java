package com.example.SmartShop.controller;

import com.example.SmartShop.dto.order.OrderCreateDTO;
import com.example.SmartShop.dto.order.OrderDTO;
import com.example.SmartShop.dto.order.OrderUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.exception.AccessDeniedException;
import com.example.SmartShop.service.OrderService;
import com.example.SmartShop.service.UserService;
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
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    // -------------------------------
    // Vérification ADMIN via HttpSession
    // -------------------------------
        private void checkAdmin(HttpSession session) {
            UserDTO currentUser = userService.getCurrentUser(session);
            if (currentUser.getRole() != UserRole.ADMIN) {
                throw new AccessDeniedException("Accès refusé");
            }
        }

        // -------------------------------
    // CREATE ORDER (ADMIN ONLY)
    // -------------------------------
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @Valid @RequestBody OrderCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderDTO created = orderService.create(dto);
        return ResponseEntity.ok(created);
    }

    // -------------------------------
    // GET ORDER BY ID (ADMIN ONLY)
    // -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        OrderDTO order = orderService.getById(id);
        return ResponseEntity.ok(order);
    }

    // -------------------------------
    // GET ALL ORDERS (ADMIN ONLY, paginated)
    // -------------------------------
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<OrderDTO> orders = orderService.getAll(pageable);
        return ResponseEntity.ok(orders);
    }

    // -------------------------------
    // UPDATE ORDER (ADMIN ONLY)
    // -------------------------------
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

    // -------------------------------
    // DELETE ORDER (ADMIN ONLY)
    // -------------------------------
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
