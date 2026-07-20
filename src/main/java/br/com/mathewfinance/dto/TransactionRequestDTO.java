package br.com.mathewfinance.dto;

import br.com.mathewfinance.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(

        @NotNull(message = "Valor é obrigatório")
        BigDecimal amount,

        @NotBlank(message = "Descrição é obrigatória")
        String description,

        @NotNull(message = "Tipo é obrigatório")
        TransactionType type,

        @NotNull(message = "Categoria é obrigatória")
        Long categoryId,

        @NotNull(message = "Data é obrigatória")
        LocalDate transactionDate,

        @NotNull(message = "Usuário é obrigatório")
        Long userId
) {}
