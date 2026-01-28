package com.jonathanleite.clientapi.domain.service;

import com.jonathanleite.clientapi.api.dto.ClientRequestDTO;
import com.jonathanleite.clientapi.api.dto.ClientResponseDTO;

import java.util.List;

public interface ClientService {

    ClientResponseDTO create(ClientRequestDTO request);

    ClientResponseDTO update(Long id, ClientRequestDTO request);

    ClientResponseDTO findById(Long id);

    List<ClientResponseDTO> findAll();

    void delete(Long id);
}
