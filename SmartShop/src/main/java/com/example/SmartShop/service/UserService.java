package com.example.SmartShop.service;

import com.example.SmartShop.dto.UserDTO;
import com.example.SmartShop.dto.UserLoginDTO;
import com.example.SmartShop.dto.UserRegisterDTO;

import jakarta.servlet.http.HttpSession;

public interface UserService {

    UserDTO register(UserRegisterDTO dto);

    UserDTO login(UserLoginDTO dto, HttpSession session);

    void logout(HttpSession session);

    UserDTO getCurrentUser(HttpSession session);
}
