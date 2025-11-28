package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.order.OrderCreateDTO;
import com.example.SmartShop.dto.order.OrderDTO;
import com.example.SmartShop.dto.order.OrderUpdateDTO;
import com.example.SmartShop.entity.Order;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
public interface OrderMapper {

    // -------------------------------
    // Entity -> DTO
    // -------------------------------
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "items", target = "items")
    OrderDTO toDTO(Order entity);


    // -------------------------------
    // Create DTO -> Entity
    // -------------------------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true) // on injectera le client depuis service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "totalDiscount", ignore = true)
    @Mapping(target = "netHt", ignore = true)
    @Mapping(target = "vat", ignore = true)
    @Mapping(target = "totalTtc", ignore = true)
    @Mapping(target = "amountRemaining", ignore = true)
    @Mapping(target = "status", ignore = true) // status = PENDING par défaut entity
    @Mapping(target = "items", ignore = true) // items ajoutés dans service
    Order toEntity(OrderCreateDTO dto);


    // -------------------------------
    // Update DTO -> Entity (PATCH)
    // -------------------------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "items", ignore = true) // update items géré séparément
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "totalDiscount", ignore = true)
    @Mapping(target = "netHt", ignore = true)
    @Mapping(target = "vat", ignore = true)
    @Mapping(target = "totalTtc", ignore = true)
    @Mapping(target = "amountRemaining", ignore = true)
    void updateEntityFromDTO(OrderUpdateDTO dto, @MappingTarget Order entity);
}
