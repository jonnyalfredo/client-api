package com.jonathanleite.clientapi.api.controller;

import com.jonathanleite.clientapi.api.dto.ClientPatchRequestDTO;
import com.jonathanleite.clientapi.api.dto.ClientRequestDTO;
import com.jonathanleite.clientapi.api.dto.ClientResponseDTO;
import com.jonathanleite.clientapi.domain.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(
            @Valid @RequestBody ClientRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO request) {

        return ResponseEntity.ok(clientService.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> patch(
            @PathVariable Long id,
            @RequestBody ClientPatchRequestDTO request) {

        return ResponseEntity.ok(clientService.patch(id, request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> findAll(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String document,
            Pageable pageable) {

        return ResponseEntity.ok(
                clientService.findAll(email, document, pageable)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
