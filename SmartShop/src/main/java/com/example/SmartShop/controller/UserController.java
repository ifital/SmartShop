package com.example.SmartShop.controller;

import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.dto.user.UserLoginDTO;
import com.example.SmartShop.dto.user.UserRegisterDTO;
import com.example.SmartShop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'inscription, la connexion et la gestion de session")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Inscription utilisateur", description = "Permet à un nouvel utilisateur de s'inscrire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur inscrit avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        UserDTO registeredUser = userService.register(dto);
        return ResponseEntity.ok(registeredUser);
    }

    @Operation(summary = "Connexion utilisateur", description = "Permet à un utilisateur de se connecter et de créer une session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody UserLoginDTO dto, HttpSession session) {
        UserDTO loggedUser = userService.login(dto, session);
        return ResponseEntity.ok(loggedUser);
    }

    @Operation(summary = "Déconnexion utilisateur", description = "Supprime la session de l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Déconnexion réussie")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer l'utilisateur connecté", description = "Retourne les informations de l'utilisateur actuellement connecté")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur connecté récupéré"),
            @ApiResponse(responseCode = "401", description = "Aucun utilisateur connecté")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        return ResponseEntity.ok(currentUser);
    }
}
