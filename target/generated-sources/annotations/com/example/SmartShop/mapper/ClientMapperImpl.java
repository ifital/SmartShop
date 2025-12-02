package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.client.ClientCreateDTO;
import com.example.SmartShop.dto.client.ClientDTO;
import com.example.SmartShop.dto.client.ClientUpdateDTO;
import com.example.SmartShop.entity.Client;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T17:19:30+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientDTO toDTO(Client client) {
        if ( client == null ) {
            return null;
        }

        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setId( client.getId() );
        clientDTO.setName( client.getName() );
        clientDTO.setEmail( client.getEmail() );

        clientDTO.setTier( client.getTier().name() );

        return clientDTO;
    }

    @Override
    public Client toEntity(ClientCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Client.ClientBuilder<?, ?> client = Client.builder();

        client.name( dto.getName() );
        client.email( dto.getEmail() );
        client.tier( dto.getTier() );

        return client.build();
    }

    @Override
    public void updateEntityFromDTO(ClientUpdateDTO dto, Client client) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getName() != null ) {
            client.setName( dto.getName() );
        }
        if ( dto.getEmail() != null ) {
            client.setEmail( dto.getEmail() );
        }
        if ( dto.getTier() != null ) {
            client.setTier( dto.getTier() );
        }
    }
}
