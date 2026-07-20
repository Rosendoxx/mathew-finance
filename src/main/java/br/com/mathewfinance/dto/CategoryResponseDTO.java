package br.com.mathewfinance.dto;

import br.com.mathewfinance.model.CategoryType;
import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Long id,
        String name,
        String color,
        CategoryType type,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
