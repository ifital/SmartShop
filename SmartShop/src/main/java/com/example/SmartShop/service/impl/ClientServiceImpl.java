package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.ClientCreateDTO;
import com.example.SmartShop.dto.ClientDTO;
import com.example.SmartShop.dto.ClientUpdateDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.mapper.ClientMapper;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientDTO create(ClientCreateDTO dto) {

        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        Client client = clientMapper.toEntity(dto);
        Client saved = clientRepository.save(client);

        return clientMapper.toDTO(saved);
    }

    @Override
    public ClientDTO update(String id, ClientUpdateDTO dto) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        // update partiel grâce à MapStruct
        clientMapper.updateEntityFromDTO(dto, client);

        Client updated = clientRepository.save(client);

        return clientMapper.toDTO(updated);
    }

    @Override
    public ClientDTO getById(String id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
    }

    @Override
    public List<ClientDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(String id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client introuvable");
        }
        clientRepository.deleteById(id);
    }
}
