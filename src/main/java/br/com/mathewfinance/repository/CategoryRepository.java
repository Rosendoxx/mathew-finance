package br.com.mathewfinance.repository;

import br.com.mathewfinance.model.Category;
import br.com.mathewfinance.model.CategoryType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public PanacheQuery<Category> findByUserId(Long userId) {
        return find("user.id", userId);
    }

    public PanacheQuery<Category> findByName(String name) {
        return find("LOWER(name) LIKE LOWER(?1)", "%" + name + "%");
    }

    public PanacheQuery<Category> findByType(CategoryType type) {
        return find("type", type);
    }

    public PanacheQuery<Category> findByUserIdAndType(Long userId, CategoryType type) {
        return find("user.id = ?1 AND type = ?2", userId, type);
    }

    public PanacheQuery<Category> findAllCategories() {
        return findAll();
    }
}
