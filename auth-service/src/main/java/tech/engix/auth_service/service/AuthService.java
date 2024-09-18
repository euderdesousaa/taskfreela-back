package tech.engix.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.UserResponseDTO;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.mapper.UserMapper;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.repositories.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(SignUpDto dto) {
        User user = mapper.toEntityInsert(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        User savedUser = repository.save(user);
        return mapper.toInsertDto(savedUser);
    }
}
