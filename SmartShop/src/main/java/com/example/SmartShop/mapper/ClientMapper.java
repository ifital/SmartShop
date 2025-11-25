package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.ClientDTO;
import com.example.SmartShop.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDto(Client entity);
    Client toEntity(ClientDTO dto);
}
