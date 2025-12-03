package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.order.OrderCreateDTO;
import com.example.SmartShop.dto.order.OrderDTO;
import com.example.SmartShop.dto.order.OrderUpdateDTO;
import com.example.SmartShop.dto.orderItem.OrderItemDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T16:04:27+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderDTO toDTO(Order entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setClientId( entityClientId( entity ) );
        orderDTO.setItems( orderItemListToOrderItemDTOList( entity.getItems() ) );
        orderDTO.setId( entity.getId() );
        orderDTO.setCreatedAt( entity.getCreatedAt() );
        orderDTO.setSubTotal( entity.getSubTotal() );
        orderDTO.setTotalDiscount( entity.getTotalDiscount() );
        orderDTO.setNetHt( entity.getNetHt() );
        orderDTO.setVat( entity.getVat() );
        orderDTO.setTotalTtc( entity.getTotalTtc() );
        orderDTO.setPromoCode( entity.getPromoCode() );
        orderDTO.setStatus( entity.getStatus() );
        orderDTO.setAmountRemaining( entity.getAmountRemaining() );

        return orderDTO;
    }

    @Override
    public Order toEntity(OrderCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.promoCode( dto.getPromoCode() );

        return order.build();
    }

    @Override
    public void updateEntityFromDTO(OrderUpdateDTO dto, Order entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getPromoCode() != null ) {
            entity.setPromoCode( dto.getPromoCode() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
    }

    private String entityClientId(Order order) {
        if ( order == null ) {
            return null;
        }
        Client client = order.getClient();
        if ( client == null ) {
            return null;
        }
        String id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<OrderItemDTO> orderItemListToOrderItemDTOList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDTO> list1 = new ArrayList<OrderItemDTO>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemMapper.toDTO( orderItem ) );
        }

        return list1;
    }
}
