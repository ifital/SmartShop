package com.example.SmartShop.service;

import com.example.SmartShop.dto.ClientCreateDTO;
import com.example.SmartShop.dto.ClientDTO;
import com.example.SmartShop.dto.ClientUpdateDTO;

import java.util.List;

public interface ClientService {

    ClientDTO create(ClientCreateDTO dto);

    ClientDTO update(String id, ClientUpdateDTO dto);

    ClientDTO getById(String id);

    List<ClientDTO> getAll();

    void delete(String id);
}
