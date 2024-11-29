package tech.engix.auth_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.engix.auth_service.controller.exception.exceptions.ServerErrorException;
import tech.engix.auth_service.dto.request.ChangePasswordRequest;
import tech.engix.auth_service.dto.responses.UserClientResponse;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.mapper.UserMapper;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUser_Success() {
        String email = "test@example.com";
        User existingUser = new User();
        UserUpdateDTO dto = new UserUpdateDTO("test", "test@example.com");

        when(repository.findByEmail(email)).thenReturn(existingUser);
        when(repository.save(existingUser)).thenReturn(existingUser);

        userService.updateUser(email, dto);

        verify(repository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        String email = "nonexistent@example.com";
        UserUpdateDTO dto = new UserUpdateDTO("test", "test@example.com");

        when(repository.findByEmail(email)).thenReturn(null);

        assertThrows(ServerErrorException.class, () -> userService.updateUser(email, dto));
    }

    @Test
    void testGetUserNameByUsername_Success() {
        String username = "testuser";
        User user = new User();
        user.setName("Test User");

        when(repository.findByEmail(username)).thenReturn(user);

        String result = userService.getUserNameByUsername(username);

        assertEquals("Test User", result);
    }

    @Test
    void testGetUserNameByUsername_UserNotFound() {
        String username = "nonexistent";

        when(repository.findByEmail(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserNameByUsername(username));
    }

    @Test
    void testUpdatePassword_Success() {
        String username = "testuser";
        ChangePasswordRequest dto = new ChangePasswordRequest("currentPassword","newPassword", "newPassword");
        User user = new User();
        user.setPassword("encodedCurrentPassword");

        when(repository.findByEmail(username)).thenReturn(user);
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.updatePassword(username, dto);

        assertEquals("encodedNewPassword", user.getPassword());
        verify(repository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword_IncorrectCurrentPassword() {
        String username = "testuser";
        ChangePasswordRequest dto = new ChangePasswordRequest("newPassword","wrongPassword", "newPassword");
        User user = new User();
        user.setPassword("encodedCurrentPassword");

        when(repository.findByEmail(username)).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.updatePassword(username, dto));
    }

    @Test
    void testGetUserByEmail_Success() {
        String email = "test@example.com";
        User user = new User();
        UserClientResponse response = new UserClientResponse("teste", "test@example.com", 1L);

        when(repository.findByEmail(email)).thenReturn(user);
        when(mapper.toClientResponse(user)).thenReturn(response);

        UserClientResponse result = userService.getUserByEmail(email);

        assertEquals(response, result);
    }

}

