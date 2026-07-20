package br.com.mathewfinance.service;

import br.com.mathewfinance.dto.CategoryRequestDTO;
import br.com.mathewfinance.dto.CategoryResponseDTO;
import br.com.mathewfinance.mapper.CategoryMapper;
import br.com.mathewfinance.model.Category;
import br.com.mathewfinance.model.CategoryType;
import br.com.mathewfinance.model.User;
import br.com.mathewfinance.repository.CategoryRepository;
import br.com.mathewfinance.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CategoryMapper categoryMapper;

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAllCategories()
                .list()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
        return categoryRepository.findByIdOptional(id)
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada: " + id));
    }

    public List<CategoryResponseDTO> findByUserId(Long userId) {
        return categoryRepository.findByUserId(userId)
                .list()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> findByName(String name) {
        return categoryRepository.findByName(name)
                .list()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> findByType(CategoryType type) {
        return categoryRepository.findByType(type)
                .list()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> findByUserIdAndType(Long userId, CategoryType type) {
        return categoryRepository.findByUserIdAndType(userId, type)
                .list()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        User user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + dto.userId()));
        Category category = categoryMapper.toEntity(dto, user);
        categoryRepository.persist(category);
        return categoryMapper.toResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = categoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada: " + id));
        User user = userRepository.findByIdOptional(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + dto.userId()));
        categoryMapper.updateEntity(category, dto, user);
        categoryRepository.persist(category);
        return categoryMapper.toResponseDTO(category);
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.deleteById(id)) {
            throw new NotFoundException("Categoria não encontrada: " + id);
        }
    }
}
