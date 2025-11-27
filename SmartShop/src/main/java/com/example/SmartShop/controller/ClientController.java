package com.example.SmartShop.controller;

import com.example.SmartShop.dto.ClientCreateDTO;
import com.example.SmartShop.dto.ClientDTO;
import com.example.SmartShop.dto.ClientUpdateDTO;
import com.example.SmartShop.dto.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.ClientService;
import com.example.SmartShop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UserService userService; // pour vérifier le rôle

    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(
            @Valid @RequestBody ClientCreateDTO dto,
            HttpSession session
    ) {
        checkAdmin(session);
        ClientDTO created = clientService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClient(
            @PathVariable String id,
            HttpSession session
    ) {
        checkAdmin(session);
        ClientDTO client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> getAllClients(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpSession session
    ) {
        checkAdmin(session);
        Page<ClientDTO> clients = clientService.getAll(pageable);
        return ResponseEntity.ok(clients);
    }

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
