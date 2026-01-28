package com.jonathanleite.clientapi.domain.service;

import com.jonathanleite.clientapi.api.dto.ClientRequestDTO;
import com.jonathanleite.clientapi.api.dto.ClientResponseDTO;
import com.jonathanleite.clientapi.domain.entity.Client;
import com.jonathanleite.clientapi.domain.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientResponseDTO create(ClientRequestDTO request) {

        validateDuplicateClient(request);

        Client client = Client.builder()
                .name(request.getName())
                .document(request.getDocument())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        Client savedClient = clientRepository.save(client);

        return toResponseDTO(savedClient);
    }

    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO request) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        validateDuplicateClientUpdate(id, request);

        client.setName(request.getName());
        client.setDocument(request.getDocument());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());

        Client updatedClient = clientRepository.save(client);

        return toResponseDTO(updatedClient);
    }

    @Override
    public ClientResponseDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        return toResponseDTO(client);
    }

    @Override
    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }

        clientRepository.deleteById(id);
    }

    /* ===============================
       MÉTODOS AUXILIARES
       =============================== */

    private void validateDuplicateClient(ClientRequestDTO request) {

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (clientRepository.existsByDocument(request.getDocument())) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }
    }

    private void validateDuplicateClientUpdate(Long id, ClientRequestDTO request) {

        clientRepository.findByEmail(request.getEmail())
                .filter(client -> !client.getId().equals(id))
                .ifPresent(client -> {
                    throw new IllegalArgumentException("Email já cadastrado");
                });

        clientRepository.findByDocument(request.getDocument())
                .filter(client -> !client.getId().equals(id))
                .ifPresent(client -> {
                    throw new IllegalArgumentException("Documento já cadastrado");
                });
    }

    private ClientResponseDTO toResponseDTO(Client client) {

        return ClientResponseDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .document(client.getDocument())
                .email(client.getEmail())
                .phone(client.getPhone())
                .active(client.getActive())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }
}
