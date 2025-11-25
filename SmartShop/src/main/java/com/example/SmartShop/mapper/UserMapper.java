package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.UserDTO;
import com.example.SmartShop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User entity);
    User toEntity(UserDTO dto);
}

