package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.ProductCreateDTO;
import com.example.SmartShop.dto.ProductDTO;
import com.example.SmartShop.dto.ProductUpdateDTO;
import com.example.SmartShop.entity.Product;
import com.example.SmartShop.mapper.ProductMapper;
import com.example.SmartShop.repository.OrderItemRepository;
import com.example.SmartShop.repository.ProductRepository;
import com.example.SmartShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public ProductDTO create(ProductCreateDTO dto) {
        if (productRepository.existsByNameAndDeletedFalse(dto.getName())) {
            throw new RuntimeException("Produit déjà existant !");
        }

        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);

        return productMapper.toDTO(saved);
    }

    @Override
    public ProductDTO update(String id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        productMapper.updateEntityFromDTO(dto, product);
        Product updated = productRepository.save(product);

        return productMapper.toDTO(updated);
    }

    @Override
    public ProductDTO getById(String id) {
        Product product = productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        return productMapper.toDTO(product);
    }

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        return productRepository.findAllByDeletedFalse(pageable)
                .map(productMapper::toDTO);
    }

    @Override
    public void delete(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        boolean hasOrders = orderItemRepository.existsByProductId(id);

        if (hasOrders) {
            // SOFT DELETE
            product.setDeleted(true);
            productRepository.save(product);
        } else {
            // HARD DELETE
            productRepository.delete(product);
        }
    }
}
