package tech.engix.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.engix.auth_service.dto.LoginDto;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.responses.LoginResponseDTO;
import tech.engix.auth_service.dto.responses.UserResponseDTO;
import tech.engix.auth_service.security.jwt.JwtUtils;
import tech.engix.auth_service.security.jwt.service.RefreshTokenService;
import tech.engix.auth_service.service.AuthService;
import tech.engix.auth_service.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    @InjectMocks
    private AuthController controller;

    @Mock
    private AuthService service;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAuthenticateUser() throws Exception {
        LoginDto loginDto = new LoginDto("testuser@gmail.com", "password");
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";
        String name = "Test User";

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(accessToken);
        when(jwtUtils.generateRefreshToken(authentication)).thenReturn(refreshToken);
        when(userService.getUserNameByUsername(any())).thenReturn(name);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(new LoginResponseDTO(accessToken, refreshToken, name))));
    }
}
