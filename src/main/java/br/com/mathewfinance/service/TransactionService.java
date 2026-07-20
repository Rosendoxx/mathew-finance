package br.com.mathewfinance.service;

import br.com.mathewfinance.dto.TransactionRequestDTO;
import br.com.mathewfinance.dto.TransactionResponseDTO;
import br.com.mathewfinance.mapper.TransactionMapper;
import br.com.mathewfinance.model.Category;
import br.com.mathewfinance.model.Transaction;
import br.com.mathewfinance.model.TransactionType;
import br.com.mathewfinance.model.User;
import br.com.mathewfinance.repository.CategoryRepository;
import br.com.mathewfinance.repository.TransactionRepository;
import br.com.mathewfinance.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    TransactionMapper transactionMapper;

    public List<TransactionResponseDTO> findAll() {
        return transactionRepository.findAllTransactions()
                .list()
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO findById(Long id) {
        return transactionRepository.findByIdOptional(id)
                .map(transactionMapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Transação não encontrada: " + id));
    }

    public List<TransactionResponseDTO> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId)
                .list()
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> findByUserIdAndType(Long userId, TransactionType type) {
        return transactionRepository.findByUserIdAndType(userId, type)
                .list()
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> findByCategoryId(Long categoryId) {
        return transactionRepository.findByCategoryId(categoryId)
                .list()
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> findByDateRange(Long userId, LocalDate start, LocalDate end) {
        return transactionRepository.findByDateRange(userId, start, end)
                .list()
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> findByUserIdAndDateRangeAndType(
            Long userId, LocalDate start, LocalDate end, TransactionType type) {
        return transactionRepository.findByUserIdAndDateRangeAndType(userId, start, end, type)
                .list()
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> findByDescription(Long userId, String description) {
        return transactionRepository.findByDescription(userId, description)
                .list()
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponseDTO create(TransactionRequestDTO dto) {
        User user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + dto.userId()));
        Category category = categoryRepository.findByIdOptional(dto.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada: " + dto.categoryId()));
        Transaction transaction = transactionMapper.toEntity(dto, user, category);
        transactionRepository.persist(transaction);
        return transactionMapper.toResponseDTO(transaction);
    }

    @Transactional
    public TransactionResponseDTO update(Long id, TransactionRequestDTO dto) {
        Transaction transaction = transactionRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Transação não encontrada: " + id));
        User user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + dto.userId()));
        Category category = categoryRepository.findByIdOptional(dto.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada: " + dto.categoryId()));
        transactionMapper.updateEntity(transaction, dto, user, category);
        transactionRepository.persist(transaction);
        return transactionMapper.toResponseDTO(transaction);
    }

    @Transactional
    public void delete(Long id) {
        if (!transactionRepository.deleteById(id)) {
            throw new NotFoundException("Transação não encontrada: " + id);
        }
    }
}
