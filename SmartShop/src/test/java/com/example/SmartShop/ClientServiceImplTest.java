package com.example.SmartShop;

import com.example.SmartShop.dto.client.ClientCreateDTO;
import com.example.SmartShop.dto.client.ClientDTO;
import com.example.SmartShop.dto.client.ClientUpdateDTO;
import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.enums.CustomerTier;
import com.example.SmartShop.exception.ClientNotFoundException;
import com.example.SmartShop.exception.EmailAlreadyUsedException;
import com.example.SmartShop.mapper.ClientMapper;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------------------------------------------------
    // TEST: CREATE OK
    // ----------------------------------------------------------
    @Test
    void testCreateClientSuccess() {
        ClientCreateDTO dto = new ClientCreateDTO("ACME", "acme@gmail.com", CustomerTier.SILVER);
        Client client = Client.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .tier(dto.getTier())
                .build();

        Client saved = Client.builder()
                .id("123")
                .name("ACME")
                .email("acme@gmail.com")
                .tier(CustomerTier.SILVER)
                .build();

        ClientDTO mappedDto = new ClientDTO();
        mappedDto.setId("123");
        mappedDto.setName("ACME");
        mappedDto.setEmail("acme@gmail.com");
        mappedDto.setTier("SILVER");

        when(clientRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(clientMapper.toEntity(dto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(saved);
        when(clientMapper.toDTO(saved)).thenReturn(mappedDto);

        ClientDTO result = clientService.create(dto);

        assertEquals("123", result.getId());
        assertEquals("ACME", result.getName());
        assertEquals("SILVER", result.getTier());

        verify(clientRepository, times(1)).save(client);
    }

    // ----------------------------------------------------------
    // TEST: CREATE — EMAIL DÉJÀ UTILISÉ
    // ----------------------------------------------------------
    @Test
    void testCreateEmailAlreadyUsed() {
        ClientCreateDTO dto = new ClientCreateDTO("ACME", "acme@gmail.com", CustomerTier.SILVER);

        when(clientRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> clientService.create(dto));

        verify(clientRepository, never()).save(any());
    }

    // ----------------------------------------------------------
    // TEST: UPDATE OK
    // ----------------------------------------------------------
    @Test
    void testUpdateClientSuccess() {
        ClientUpdateDTO updateDTO = new ClientUpdateDTO();
        updateDTO.setName("New Name");

        Client client = Client.builder()
                .id("123")
                .name("Old Name")
                .email("acme@gmail.com")
                .tier(CustomerTier.BASIC)
                .build();

        Client updated = Client.builder()
                .id("123")
                .name("New Name")
                .email("acme@gmail.com")
                .tier(CustomerTier.BASIC)
                .build();

        ClientDTO dto = new ClientDTO();
        dto.setId("123");
        dto.setName("New Name");

        when(clientRepository.findById("123")).thenReturn(Optional.of(client));
        doAnswer(invocation -> {
            client.setName("New Name");  // simulation de update
            return null;
        }).when(clientMapper).updateEntityFromDTO(updateDTO, client);

        when(clientRepository.save(client)).thenReturn(updated);
        when(clientMapper.toDTO(updated)).thenReturn(dto);

        ClientDTO result = clientService.update("123", updateDTO);

        assertEquals("New Name", result.getName());
    }

    // ----------------------------------------------------------
    // TEST: UPDATE — CLIENT INCONNU
    // ----------------------------------------------------------
    @Test
    void testUpdateClientNotFound() {
        ClientUpdateDTO updateDTO = new ClientUpdateDTO();

        when(clientRepository.findById("xxx")).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.update("xxx", updateDTO));
    }

    // ----------------------------------------------------------
    // TEST: GET BY ID OK
    // ----------------------------------------------------------
    @Test
    void testGetByIdSuccess() {
        Client client = Client.builder()
                .id("123")
                .name("ACME")
                .email("acme@gmail.com")
                .tier(CustomerTier.SILVER)
                .build();

        ClientDTO dto = new ClientDTO();
        dto.setId("123");
        dto.setName("ACME");

        when(clientRepository.findById("123")).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(dto);

        ClientDTO result = clientService.getById("123");

        assertEquals("123", result.getId());
        verify(clientRepository, times(1)).findById("123");
    }

    // ----------------------------------------------------------
    // TEST: GET BY ID — CLIENT INCONNU
    // ----------------------------------------------------------
    @Test
    void testGetByIdNotFound() {
        when(clientRepository.findById("xxx")).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.getById("xxx"));
    }

    // ----------------------------------------------------------
    // TEST: GET ALL PAGÉS
    // ----------------------------------------------------------
    @Test
    void testGetAll() {
        Pageable pageable = PageRequest.of(0, 10);

        Client client = Client.builder()
                .id("123")
                .name("ACME")
                .email("acme@gmail.com")
                .tier(CustomerTier.SILVER)
                .build();

        ClientDTO dto = new ClientDTO();
        dto.setId("123");

        Page<Client> page = new PageImpl<>(java.util.List.of(client));

        when(clientRepository.findAll(pageable)).thenReturn(page);
        when(clientMapper.toDTO(client)).thenReturn(dto);

        Page<ClientDTO> result = clientService.getAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    // ----------------------------------------------------------
    // TEST: DELETE OK
    // ----------------------------------------------------------
    @Test
    void testDeleteSuccess() {
        when(clientRepository.existsById("123")).thenReturn(true);

        clientService.delete("123");

        verify(clientRepository, times(1)).deleteById("123");
    }

    // ----------------------------------------------------------
    // TEST: DELETE — CLIENT INCONNU
    // ----------------------------------------------------------
    @Test
    void testDeleteClientNotFound() {
        when(clientRepository.existsById("xxx")).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> clientService.delete("xxx"));
    }
}
