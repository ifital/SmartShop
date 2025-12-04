package com.example.SmartShop.controller;

import com.example.SmartShop.dto.client.ClientCreateDTO;
import com.example.SmartShop.dto.client.ClientDTO;
import com.example.SmartShop.dto.client.ClientUpdateDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.ClientService;
import com.example.SmartShop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService; // pour vérifier le rôle

    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    @Operation(summary = "Créer un client", description = "Permet à un admin de créer un nouveau client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(
            @Valid @RequestBody ClientCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        ClientDTO created = clientService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Récupérer un client par ID", description = "Permet à un admin de récupérer les informations d'un client")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClient(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        ClientDTO client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @Operation(summary = "Récupérer tous les clients", description = "Permet à un admin de récupérer la liste paginée des clients")
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> getAllClients(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<ClientDTO> clients = clientService.getAll(pageable);
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Mettre à jour un client", description = "Permet à un admin de mettre à jour un client existant")
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable String id,
            @Valid @RequestBody ClientUpdateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        ClientDTO updated = clientService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer un client", description = "Permet à un admin de supprimer un client")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
