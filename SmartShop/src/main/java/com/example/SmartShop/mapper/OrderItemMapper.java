package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.OrderItemDTO;
import com.example.SmartShop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemDTO toDto(OrderItem entity);

    @Mapping(source = "productId", target = "product.id")
    OrderItem toEntity(OrderItemDTO dto);
}

