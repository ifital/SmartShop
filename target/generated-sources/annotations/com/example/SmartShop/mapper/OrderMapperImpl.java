package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.OrderDTO;
import com.example.SmartShop.dto.OrderItemDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.Order;
import com.example.SmartShop.entity.OrderItem;
import com.example.SmartShop.entity.enums.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T17:27:45+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderDTO toDto(Order entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        String id = entityClientId( entity );
        if ( id != null ) {
            orderDTO.setClientId( Long.parseLong( id ) );
        }
        orderDTO.setClientName( entityClientName( entity ) );
        if ( entity.getId() != null ) {
            orderDTO.setId( Long.parseLong( entity.getId() ) );
        }
        orderDTO.setSubTotal( entity.getSubTotal() );
        orderDTO.setPromoCode( entity.getPromoCode() );
        if ( entity.getStatus() != null ) {
            orderDTO.setStatus( entity.getStatus().name() );
        }
        orderDTO.setItems( orderItemListToOrderItemDTOList( entity.getItems() ) );

        return orderDTO;
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.client( orderDTOToClient( dto ) );
        if ( dto.getId() != null ) {
            order.id( String.valueOf( dto.getId() ) );
        }
        order.items( orderItemDTOListToOrderItemList( dto.getItems() ) );
        order.subTotal( dto.getSubTotal() );
        order.promoCode( dto.getPromoCode() );
        if ( dto.getStatus() != null ) {
            order.status( Enum.valueOf( OrderStatus.class, dto.getStatus() ) );
        }

        return order.build();
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

    private String entityClientName(Order order) {
        if ( order == null ) {
            return null;
        }
        Client client = order.getClient();
        if ( client == null ) {
            return null;
        }
        String name = client.getName();
        if ( name == null ) {
            return null;
        }
        return name;
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

    protected Client orderDTOToClient(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Client.ClientBuilder<?, ?> client = Client.builder();

        if ( orderDTO.getClientId() != null ) {
            client.id( String.valueOf( orderDTO.getClientId() ) );
        }

        return client.build();
    }

    protected OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO) {
        if ( orderItemDTO == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.id( orderItemDTO.getId() );
        orderItem.quantity( orderItemDTO.getQuantity() );
        orderItem.unitPrice( orderItemDTO.getUnitPrice() );
        orderItem.lineTotal( orderItemDTO.getLineTotal() );

        return orderItem.build();
    }

    protected List<OrderItem> orderItemDTOListToOrderItemList(List<OrderItemDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemDTO orderItemDTO : list ) {
            list1.add( orderItemDTOToOrderItem( orderItemDTO ) );
        }

        return list1;
    }
}
