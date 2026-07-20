package br.com.mathewfinance.dto;

import java.time.LocalDateTime;

import br.com.mathewfinance.model.SystemRole;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        SystemRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
