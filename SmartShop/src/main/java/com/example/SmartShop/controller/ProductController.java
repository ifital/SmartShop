package com.example.SmartShop.controller;

import com.example.SmartShop.dto.ProductCreateDTO;
import com.example.SmartShop.dto.ProductDTO;
import com.example.SmartShop.dto.ProductUpdateDTO;
import com.example.SmartShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // -------------------------------
    // CREATE
    // -------------------------------
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateDTO dto) {
        ProductDTO created = productService.create(dto);
        return ResponseEntity.ok(created);
    }

    // -------------------------------
    // GET BY ID
    // -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String id) {
        ProductDTO product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // -------------------------------
    // GET ALL
    // -------------------------------
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<ProductDTO> products = productService.getAll(pageable);
        return ResponseEntity.ok(products);
    }

    // -------------------------------
    // UPDATE
    // -------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductUpdateDTO dto
    ) {
        ProductDTO updated = productService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // -------------------------------
    // DELETE
    // -------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
