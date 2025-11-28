package com.example.SmartShop.service;

import com.example.SmartShop.dto.client.ClientCreateDTO;
import com.example.SmartShop.dto.client.ClientDTO;
import com.example.SmartShop.dto.client.ClientUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    ClientDTO create(ClientCreateDTO dto);

    ClientDTO update(String id, ClientUpdateDTO dto);

    ClientDTO getById(String id);

    Page<ClientDTO> getAll(Pageable pageable);

    void delete(String id);
}
