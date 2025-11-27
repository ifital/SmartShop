package com.example.SmartShop.service;

import com.example.SmartShop.dto.ProductCreateDTO;
import com.example.SmartShop.dto.ProductDTO;
import com.example.SmartShop.dto.ProductUpdateDTO;

import java.util.List;

public interface ProductService {

    ProductDTO create(ProductCreateDTO dto);

    ProductDTO update(String id, ProductUpdateDTO dto);

    ProductDTO getById(String id);

    List<ProductDTO> getAll();

    void delete(String id);
}
