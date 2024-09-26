package tech.engix.auth_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.engix.auth_service.controller.exception.exceptions.CustomerNotFoundException;
import tech.engix.auth_service.controller.exception.exceptions.ServerErrorException;
import tech.engix.auth_service.dto.request.ChangePasswordRequest;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.mapper.UserMapper;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.repositories.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public void updateUser(String email, UserUpdateDTO dto) {
        User existingUser = repository.findByEmail(email);
        if (existingUser != null) {
            mapper.toEntityUpdate(dto, existingUser);

            mapper.toUpdate(repository.save(existingUser));
        } else {
            throw new ServerErrorException("Internal Server Error");
        }
    }

    public String getUserNameByUsername(String username) {
        User user = repository.findByEmail(username);
        if (user != null) {
            return user.getName();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public User getUserInfoById(Long id) {
        log.debug("Getting user info by id: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: %s.".formatted(id)));
    }

    public void updatePassword(String username, ChangePasswordRequest dto) {
        User user = repository.findByEmail(username);

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        repository.save(user);
    }

    public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
        User user = repository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            repository.save(user);
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return repository.findByResetPasswordToken(token);
    }

    public void updateRecoveryPassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        repository.save(user);
    }
}
