package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.ProductDTO;
import com.example.SmartShop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product entity);
    Product toEntity(ProductDTO dto);
}

