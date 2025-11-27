package com.example.SmartShop.service;

import com.example.SmartShop.dto.ProductCreateDTO;
import com.example.SmartShop.dto.ProductDTO;
import com.example.SmartShop.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDTO create(ProductCreateDTO dto);

    ProductDTO update(String id, ProductUpdateDTO dto);

    ProductDTO getById(String id);

    Page<ProductDTO> getAll(Pageable pageable);

    void delete(String id);
}
