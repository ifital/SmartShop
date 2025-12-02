package com.example.SmartShop.mapper;

import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.dto.user.UserRegisterDTO;
import com.example.SmartShop.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T17:19:29+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setRole( user.getRole() );

        return userDTO;
    }

    @Override
    public User toEntity(UserRegisterDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder<?, ?> user = User.builder();

        user.username( dto.getUsername() );
        user.role( dto.getRole() );

        return user.build();
    }
}
