package br.com.mathewfinance.dto;

import br.com.mathewfinance.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        String color,

        @NotNull(message = "Tipo é obrigatório")
        CategoryType type,

        @NotNull(message = "Usuário é obrigatório")
        Long userId
) {}
