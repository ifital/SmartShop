package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.client.ClientCreateDTO;
import com.example.SmartShop.dto.client.ClientDTO;
import com.example.SmartShop.dto.client.ClientUpdateDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.exception.ClientNotFoundException;
import com.example.SmartShop.exception.EmailAlreadyUsedException;
import com.example.SmartShop.exception.InvalidCredentialsException;
import com.example.SmartShop.mapper.ClientMapper;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ClientDTO create(ClientCreateDTO dto) {

        // Vérifier email unique
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyUsedException("Email déjà utilisé !");
        }

        // Vérifier username unique
        if (clientRepository.existsByUsername(dto.getUsername())) {
            throw new InvalidCredentialsException("Nom d'utilisateur déjà utilisé !");
        }

        // Mapper DTO → Entity
        Client client = clientMapper.toEntity(dto);

        // Encoder le mot de passe
        client.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Convertir le rôle en Enum
        if (dto.getRole() != null) {
            client.setRole(UserRole.valueOf(dto.getRole()));
        }

        // Sauvegarder
        Client saved = clientRepository.save(client);

        return clientMapper.toDTO(saved);
    }

    @Override
    public ClientDTO update(String id, ClientUpdateDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client introuvable"));

        clientMapper.updateEntityFromDTO(dto, client);

        Client updated = clientRepository.save(client);
        return clientMapper.toDTO(updated);
    }

    @Override
    public ClientDTO getById(String id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO)
                .orElseThrow(() -> new ClientNotFoundException("Client introuvable"));
    }

    @Override
    public Page<ClientDTO> getAll(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(clientMapper::toDTO);
    }

    @Override
    public void delete(String id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client introuvable");
        }
        clientRepository.deleteById(id);
    }
}
