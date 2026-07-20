package br.com.mathewfinance.mapper;

import br.com.mathewfinance.dto.CategoryRequestDTO;
import br.com.mathewfinance.dto.CategoryResponseDTO;
import br.com.mathewfinance.model.Category;
import br.com.mathewfinance.model.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryMapper {

    public CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getType(),
                category.getUser() != null ? category.getUser().getId() : null,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public Category toEntity(CategoryRequestDTO dto, User user) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.name());
        category.setColor(dto.color());
        category.setType(dto.type());
        category.setUser(user);
        return category;
    }

    public void updateEntity(Category category, CategoryRequestDTO dto, User user) {
        if (category == null || dto == null) {
            return;
        }
        category.setName(dto.name());
        category.setColor(dto.color());
        category.setType(dto.type());
        if (user != null) {
            category.setUser(user);
        }
    }
}
