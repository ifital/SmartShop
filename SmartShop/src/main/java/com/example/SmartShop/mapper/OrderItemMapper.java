package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.OrderItemCreateDTO;
import com.example.SmartShop.dto.OrderItemDTO;
import com.example.SmartShop.dto.OrderItemUpdateDTO;
import com.example.SmartShop.entity.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "orderId", source = "order.id")
    OrderItemDTO toDTO(OrderItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitPrice", ignore = true) // sera défini lors de la création
    @Mapping(target = "lineTotal", ignore = true) // sera calculé = quantity * unitPrice
    @Mapping(target = "product", ignore = true) // à lier manuellement
    @Mapping(target = "order", ignore = true)   // à lier manuellement si orderId fourni
    OrderItem toEntity(OrderItemCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(OrderItemUpdateDTO dto, @MappingTarget OrderItem entity);
}
