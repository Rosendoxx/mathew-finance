package br.com.mathewfinance.mapper;

import br.com.mathewfinance.dto.UserRequestDTO;
import br.com.mathewfinance.dto.UserResponseDTO;
import br.com.mathewfinance.model.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPasswordHash(dto.password());
        return user;
    }

    public void updateEntity(User user, UserRequestDTO dto) {
        if (user == null || dto == null) {
            return;
        }
        user.setName(dto.name());
        user.setEmail(dto.email());
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPasswordHash(dto.password());
        }
    }
}
