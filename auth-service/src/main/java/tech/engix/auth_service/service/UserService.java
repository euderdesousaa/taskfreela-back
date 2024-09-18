package tech.engix.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.engix.auth_service.dto.UserRecoveryPassword;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.mapper.UserMapper;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.repositories.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public void updateUser(String username, UserUpdateDTO dto) {
        User upd = repository.findByEmail(username);
        upd.setEmail(dto.email());
        upd.setName(dto.name());
        mapper.toUpdate(repository.save(upd));
    }

    public void updatePassword(String username, UserRecoveryPassword dto) throws Exception {
        User user = repository.findByEmail(username);

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new Exception("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        repository.save(user);
    }

   /* public void updateResetPasswordToken(String token, String email) throws Exception {
        User customer = repository.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            repository.save(customer);
        } else {
            throw new Exception();
        }
    }

    public User getByResetPasswordToken(String token) {
        return repository.findByResetPasswordToken(token);
    }

    public void updateRecoveryPassword(User customer, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
        customer.setResetPasswordToken(null);
        repository.save(customer);
    }
*/
}
