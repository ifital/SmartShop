package com.example.SmartShop.controller;

import com.example.SmartShop.dto.product.ProductCreateDTO;
import com.example.SmartShop.dto.product.ProductDTO;
import com.example.SmartShop.dto.product.ProductUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.ProductService;
import com.example.SmartShop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
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
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        ProductDTO created = productService.create(dto);
        return ResponseEntity.ok(created);
    }

    // -------------------------------
    // GET BY ID (PUBLIC)
    // -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String id) {
        ProductDTO product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // -------------------------------
    // GET ALL (PUBLIC)
    // -------------------------------
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<ProductDTO> products = productService.getAll(pageable);
        return ResponseEntity.ok(products);
    }

    // -------------------------------
    // UPDATE (ADMIN ONLY)
    // -------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductUpdateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        ProductDTO updated = productService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // -------------------------------
    // DELETE (ADMIN ONLY)
    // -------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
