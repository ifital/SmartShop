package com.example.SmartShop.controller;

import com.example.SmartShop.dto.product.ProductCreateDTO;
import com.example.SmartShop.dto.product.ProductDTO;
import com.example.SmartShop.dto.product.ProductUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "Gestion des produits")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    @Operation(summary = "Créer un produit", description = "Permet à un admin de créer un nouveau produit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        ProductDTO created = productService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Récupérer un produit par ID", description = "Permet à n'importe quel utilisateur de récupérer un produit")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String id) {
        ProductDTO product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Récupérer tous les produits", description = "Permet à n'importe quel utilisateur de récupérer la liste paginée des produits")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<ProductDTO> products = productService.getAll(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Mettre à jour un produit", description = "Permet à un admin de mettre à jour un produit existant")
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

    @Operation(summary = "Supprimer un produit", description = "Permet à un admin de supprimer un produit")
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
