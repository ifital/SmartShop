package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.OrderDTO;
import com.example.SmartShop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    OrderDTO toDto(Order entity);

    @Mapping(source = "clientId", target = "client.id")
    Order toEntity(OrderDTO dto);
}
