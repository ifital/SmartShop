package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.ProductCreateDTO;
import com.example.SmartShop.dto.ProductDTO;
import com.example.SmartShop.dto.ProductUpdateDTO;
import com.example.SmartShop.entity.Product;
import com.example.SmartShop.mapper.ProductMapper;
import com.example.SmartShop.repository.ProductRepository;
import com.example.SmartShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO create(ProductCreateDTO dto) {
        if (productRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Produit déjà existant !");
        }
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Override
    public ProductDTO update(String id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        productMapper.updateEntityFromDTO(dto, product);
        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
    }

    @Override
    public ProductDTO getById(String id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    @Override
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit introuvable");
        }
        productRepository.deleteById(id);
    }
}
