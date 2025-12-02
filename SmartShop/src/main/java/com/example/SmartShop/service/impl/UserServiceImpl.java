package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.dto.user.UserLoginDTO;
import com.example.SmartShop.dto.user.UserRegisterDTO;
import com.example.SmartShop.entity.User;
import com.example.SmartShop.exception.EmailAlreadyUsedException;
import com.example.SmartShop.exception.UserNotFoundException;
import com.example.SmartShop.exception.InvalidCredentialsException;
import com.example.SmartShop.mapper.UserMapper;
import com.example.SmartShop.repository.UserRepository;
import com.example.SmartShop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String SESSION_USER_KEY = "CURRENT_USER";

    @Override
    public UserDTO register(UserRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new EmailAlreadyUsedException("Nom d'utilisateur déjà utilisé !");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // hash du mot de passe
        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO login(UserLoginDTO dto, HttpSession session) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Nom d'utilisateur incorrect"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Mot de passe incorrect");
        }

        session.setAttribute(SESSION_USER_KEY, user.getId());
        return userMapper.toDTO(user);
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public UserDTO getCurrentUser(HttpSession session) {
        String userId = (String) session.getAttribute(SESSION_USER_KEY);
        if (userId == null) {
            throw new UserNotFoundException("Aucun utilisateur connecté");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        return userMapper.toDTO(user);
    }
}
