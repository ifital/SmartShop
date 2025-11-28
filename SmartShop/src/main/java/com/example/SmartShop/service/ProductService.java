package com.example.SmartShop.service;

import com.example.SmartShop.dto.product.ProductCreateDTO;
import com.example.SmartShop.dto.product.ProductDTO;
import com.example.SmartShop.dto.product.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDTO create(ProductCreateDTO dto);

    ProductDTO update(String id, ProductUpdateDTO dto);

    ProductDTO getById(String id);

    Page<ProductDTO> getAll(Pageable pageable);

    void delete(String id);
}
