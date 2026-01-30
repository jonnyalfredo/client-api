package com.jonathanleite.clientapi.domain.service;

import com.jonathanleite.clientapi.api.dto.ClientRequestDTO;
import com.jonathanleite.clientapi.api.dto.ClientResponseDTO;
import com.jonathanleite.clientapi.domain.entity.Client;
import com.jonathanleite.clientapi.domain.exception.ConflictException;
import com.jonathanleite.clientapi.domain.exception.ResourceNotFoundException;
import com.jonathanleite.clientapi.domain.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    /* ===============================
       CREATE
       =============================== */

    @Test
    void shouldCreateClientSuccessfully() {
        ClientRequestDTO request = ClientRequestDTO.builder()
                .email("teste@email.com")
                .document("123")
                .build();

        Client savedClient = Client.builder()
                .id(1L)
                .email(request.getEmail())
                .document(request.getDocument())
                .build();

        when(clientRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(clientRepository.save(any(Client.class)))
                .thenReturn(savedClient);

        ClientResponseDTO response = clientService.create(request);

        assertNotNull(response);
        assertEquals(savedClient.getId(), response.getId());
        assertEquals(savedClient.getEmail(), response.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        ClientRequestDTO request = ClientRequestDTO.builder()
                .email("duplicado@email.com")
                .document("123")
                .build();

        when(clientRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> clientService.create(request)
        );

        verify(clientRepository, never()).save(any());
    }

    /* ===============================
       FIND BY ID
       =============================== */

    @Test
    void shouldFindClientByIdSuccessfully() {
        Long id = 1L;

        Client client = Client.builder()
                .id(id)
                .email("teste@email.com")
                .document("123")
                .build();

        when(clientRepository.findById(id))
                .thenReturn(Optional.of(client));

        ClientResponseDTO response = clientService.findById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    void shouldThrowNotFoundWhenClientDoesNotExist() {
        Long id = 1L;

        when(clientRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.findById(id)
        );
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistingClient() {
        Long id = 1L;

        ClientRequestDTO request = ClientRequestDTO.builder()
                .email("teste@email.com")
                .document("123")
                .build();

        when(clientRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.update(id, request)
        );
    }

    /* ===============================
       FIND ALL
       =============================== */

    @Test
    void shouldReturnListOfClients() {
        Client client1 = Client.builder().id(1L).email("a@email.com").build();
        Client client2 = Client.builder().id(2L).email("b@email.com").build();

        when(clientRepository.findAll())
                .thenReturn(List.of(client1, client2));

        List<ClientResponseDTO> response = clientService.findAll();

        assertEquals(2, response.size());
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistingClient() {
        Long id = 1L;

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.delete(id)
        );
    }
}
