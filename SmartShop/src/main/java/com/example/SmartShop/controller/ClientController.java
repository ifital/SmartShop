package com.example.SmartShop.controller;

import com.example.SmartShop.dto.ClientCreateDTO;
import com.example.SmartShop.dto.ClientDTO;
import com.example.SmartShop.dto.ClientUpdateDTO;
import com.example.SmartShop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // -------------------------------
    // CREATE
    // -------------------------------
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientCreateDTO dto) {
        ClientDTO created = clientService.create(dto);
        return ResponseEntity.ok(created);
    }

    // -------------------------------
    // GET BY ID
    // -------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable String id) {
        ClientDTO client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    // -------------------------------
    // GET ALL
    // -------------------------------
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> getAllClients(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<ClientDTO> clients = clientService.getAll(pageable);
        return ResponseEntity.ok(clients);
    }

    // -------------------------------
    // UPDATE
    // -------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable String id,
            @Valid @RequestBody ClientUpdateDTO dto
    ) {
        ClientDTO updated = clientService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // -------------------------------
    // DELETE
    // -------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
