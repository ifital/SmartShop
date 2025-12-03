package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.orderItem.OrderItemCreateDTO;
import com.example.SmartShop.dto.orderItem.OrderItemDTO;
import com.example.SmartShop.dto.orderItem.OrderItemUpdateDTO;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.OrderItem;
import com.example.SmartShop.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T16:04:27+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemDTO toDTO(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();

        orderItemDTO.setProductId( itemProductId( item ) );
        orderItemDTO.setProductName( itemProductName( item ) );
        orderItemDTO.setOrderId( itemOrderId( item ) );
        orderItemDTO.setId( item.getId() );
        orderItemDTO.setQuantity( item.getQuantity() );
        orderItemDTO.setUnitPrice( item.getUnitPrice() );
        orderItemDTO.setLineTotal( item.getLineTotal() );

        return orderItemDTO;
    }

    @Override
    public OrderItem toEntity(OrderItemCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        if ( dto.getQuantity() != null ) {
            orderItem.quantity( dto.getQuantity() );
        }

        return orderItem.build();
    }

    @Override
    public void updateEntityFromDTO(OrderItemUpdateDTO dto, OrderItem entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getQuantity() != null ) {
            entity.setQuantity( dto.getQuantity() );
        }
    }

    private String itemProductId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductName(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String itemOrderId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Order order = orderItem.getOrder();
        if ( order == null ) {
            return null;
        }
        String id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
