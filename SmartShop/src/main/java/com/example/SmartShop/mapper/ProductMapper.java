package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.ProductDTO;
import com.example.SmartShop.dto.ProductCreateDTO;
import com.example.SmartShop.dto.ProductUpdateDTO;
import com.example.SmartShop.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // ENTITY -> DTO
    ProductDTO toDTO(Product product);

    // CREATE DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductCreateDTO dto);

    // UPDATE DTO -> ENTITY (partial update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProductUpdateDTO dto, @MappingTarget Product product);
}
