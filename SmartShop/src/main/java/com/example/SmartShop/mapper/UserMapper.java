package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.dto.user.UserRegisterDTO;
import com.example.SmartShop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ENTITY -> DTO
    UserDTO toDTO(User user);

    // REGISTER DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // le password sera hashé séparément
    User toEntity(UserRegisterDTO dto);
}
