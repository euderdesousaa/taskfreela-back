package tech.engix.auth_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.responses.UserResponseDTO;
import tech.engix.auth_service.mapper.UserMapper;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp() {
        SignUpDto signUpDto = new SignUpDto("testuser", "password@exemplo.com", "password");

        User mockUser = new User();
        mockUser.setName(signUpDto.name());
        mockUser.setEmail(signUpDto.email());
        mockUser.setPassword("encodedPassword");

        when(mapper.toEntityInsert(signUpDto)).thenReturn(mockUser);

        when(passwordEncoder.encode(signUpDto.password())).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponseDTO mockResponse = new UserResponseDTO("testuser", "password@exemplo.com");
        when(mapper.toInsertDto(mockUser)).thenReturn(mockResponse);

        UserResponseDTO response = service.registerUser(signUpDto);

        assertNotNull(response, "Response should not be null");
        assertEquals("testuser", response.name());
    }

}
