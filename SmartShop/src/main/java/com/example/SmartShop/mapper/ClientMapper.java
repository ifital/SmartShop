package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.client.ClientDTO;
import com.example.SmartShop.dto.client.ClientCreateDTO;
import com.example.SmartShop.dto.client.ClientUpdateDTO;
import com.example.SmartShop.entity.Client;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    // ENTITY -> DTO
    @Mapping(target = "tier", expression = "java(client.getTier().name())")
    ClientDTO toDTO(Client client);

    // CREATE DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientCreateDTO dto);

    // UPDATE DTO -> ENTITY (partial update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ClientUpdateDTO dto, @MappingTarget Client client);
}
