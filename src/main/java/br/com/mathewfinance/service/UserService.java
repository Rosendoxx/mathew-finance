package br.com.mathewfinance.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.mathewfinance.dto.UserRequestDTO;
import br.com.mathewfinance.dto.UserResponseDTO;
import br.com.mathewfinance.mapper.UserMapper;
import br.com.mathewfinance.model.User;
import br.com.mathewfinance.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    public List<UserResponseDTO> findAll() {
        return userRepository.findAllUsers()
                .list()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        return userRepository.findByIdOptional(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + id));
    }

    public List<UserResponseDTO> findByName(String name) {
        return userRepository.findByName(name)
                .list()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .firstResultOptional()
                .map(userMapper::toResponseDTO);
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        userRepository.persist(user);
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + id));
        userMapper.updateEntity(user, dto);
        userRepository.persist(user);
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.deleteById(id)) {
            throw new NotFoundException("Usuário não encontrado: " + id);
        }
    }
}
