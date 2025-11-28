package com.example.SmartShop.service;

import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.dto.user.UserLoginDTO;
import com.example.SmartShop.dto.user.UserRegisterDTO;

import jakarta.servlet.http.HttpSession;

public interface UserService {

    UserDTO register(UserRegisterDTO dto);

    UserDTO login(UserLoginDTO dto, HttpSession session);

    void logout(HttpSession session);

    UserDTO getCurrentUser(HttpSession session);
}
