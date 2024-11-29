package tech.engix.auth_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.engix.auth_service.controller.exception.exceptions.CustomerNotFoundException;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ForgotPasswordServiceTest {

    @InjectMocks
    private ForgotPasswordService service;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setResetPasswordToken("initialToken");
    }

    @Test
    void testUpdateResetPasswordToken_UserFound() throws CustomerNotFoundException {
        String newToken = "newToken";

        when(repository.findByEmail(user.getEmail())).thenReturn(user);

        service.updateResetPasswordToken(newToken, user.getEmail());

        assertEquals(newToken, user.getResetPasswordToken());
        verify(repository).save(user);
    }

    @Test
    void testUpdateResetPasswordToken_UserNotFound() {
        String email = "unknown@example.com";

        when(repository.findByEmail(email)).thenReturn(null);

        assertThrows(CustomerNotFoundException.class, () -> {
            service.updateResetPasswordToken("newToken", email);
        });
    }

    @Test
    void testGetByResetPasswordToken_UserFound() {
        when(repository.findByResetPasswordToken("initialToken")).thenReturn(user);

        User result = service.getByResetPasswordToken("initialToken");
        assertEquals(user, result);
    }

    @Test
    void testUpdateRecoveryPassword() {
        String newPassword = "newPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        service.updateRecoveryPassword(user, newPassword);

        assertEquals("encodedPassword", user.getPassword());
        assertNull(user.getResetPasswordToken());
        verify(repository).save(user);
    }
}
