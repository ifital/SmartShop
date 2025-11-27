package com.example.SmartShop.controller;

import com.example.SmartShop.dto.UserDTO;
import com.example.SmartShop.dto.UserLoginDTO;
import com.example.SmartShop.dto.UserRegisterDTO;
import com.example.SmartShop.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // -------------------------------
    // REGISTER
    // -------------------------------
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        UserDTO registeredUser = userService.register(dto);
        return ResponseEntity.ok(registeredUser);
    }

    // -------------------------------
    // LOGIN
    // -------------------------------
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody UserLoginDTO dto, HttpSession session) {
        UserDTO loggedUser = userService.login(dto, session);
        return ResponseEntity.ok(loggedUser);
    }

    // -------------------------------
    // LOGOUT
    // -------------------------------
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------
    // GET CURRENT USER
    // -------------------------------
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        return ResponseEntity.ok(currentUser);
    }
}
