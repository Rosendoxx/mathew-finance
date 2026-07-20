package br.com.mathewfinance.mapper;

import br.com.mathewfinance.dto.TransactionRequestDTO;
import br.com.mathewfinance.dto.TransactionResponseDTO;
import br.com.mathewfinance.model.Category;
import br.com.mathewfinance.model.Transaction;
import br.com.mathewfinance.model.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionMapper {

    public TransactionResponseDTO toResponseDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getCategory() != null ? transaction.getCategory().getId() : null,
                transaction.getCategory() != null ? transaction.getCategory().getName() : null,
                transaction.getTransactionDate(),
                transaction.getUser() != null ? transaction.getUser().getId() : null,
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    public Transaction toEntity(TransactionRequestDTO dto, User user, Category category) {
        if (dto == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.amount());
        transaction.setDescription(dto.description());
        transaction.setType(dto.type());
        transaction.setCategory(category);
        transaction.setTransactionDate(dto.transactionDate());
        transaction.setUser(user);
        return transaction;
    }

    public void updateEntity(Transaction transaction, TransactionRequestDTO dto,
                             User user, Category category) {
        if (transaction == null || dto == null) {
            return;
        }
        transaction.setAmount(dto.amount());
        transaction.setDescription(dto.description());
        transaction.setType(dto.type());
        transaction.setTransactionDate(dto.transactionDate());
        if (user != null) {
            transaction.setUser(user);
        }
        if (category != null) {
            transaction.setCategory(category);
        }
    }
}
