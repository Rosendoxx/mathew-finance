package br.com.mathewfinance.repository;

import br.com.mathewfinance.model.Transaction;
import br.com.mathewfinance.model.TransactionType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {

    public PanacheQuery<Transaction> findByUserId(Long userId) {
        return find("user.id", userId);
    }

    public PanacheQuery<Transaction> findByType(TransactionType type) {
        return find("type", type);
    }

    public PanacheQuery<Transaction> findByUserIdAndType(Long userId, TransactionType type) {
        return find("user.id = ?1 AND type = ?2", userId, type);
    }

    public PanacheQuery<Transaction> findByCategoryId(Long categoryId) {
        return find("category.id", categoryId);
    }

    public PanacheQuery<Transaction> findByDateRange(Long userId, LocalDate start, LocalDate end) {
        return find("user.id = ?1 AND transactionDate BETWEEN ?2 AND ?3", userId, start, end);
    }

    public PanacheQuery<Transaction> findByUserIdAndDateRangeAndType(
            Long userId, LocalDate start, LocalDate end, TransactionType type) {
        return find("user.id = ?1 AND transactionDate BETWEEN ?2 AND ?3 AND type = ?4",
                userId, start, end, type);
    }

    public PanacheQuery<Transaction> findByDescription(Long userId, String description) {
        return find("user.id = ?1 AND LOWER(description) LIKE LOWER(?2)",
                userId, "%" + description + "%");
    }

    public PanacheQuery<Transaction> findAllTransactions() {
        return findAll();
    }
}
