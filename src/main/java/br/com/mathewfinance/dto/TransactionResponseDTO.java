package br.com.mathewfinance.dto;

import br.com.mathewfinance.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        String description,
        TransactionType type,
        Long categoryId,
        String categoryName,
        LocalDate transactionDate,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
