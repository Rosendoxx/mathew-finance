package br.com.mathewfinance.repository;

import br.com.mathewfinance.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public PanacheQuery<User> findByEmail(String email) {
        return find("email", email);
    }

    public PanacheQuery<User> findByName(String name) {
        return find("LOWER(name) LIKE LOWER(?1)", "%" + name + "%");
    }

    public PanacheQuery<User> findAllUsers() {
        return findAll();
    }
}
