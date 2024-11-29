package tech.engix.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.engix.auth_service.dto.request.ForgotPasswordRequest;
import tech.engix.auth_service.dto.request.ResetPasswordRequest;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.service.ForgotPasswordService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ForgotPasswordControllerTest {


    @InjectMocks
    private ForgotPasswordController controller;

    @Mock
    private ForgotPasswordService service;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testProcessForgotPassword_UserFound() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest("test@example.com");

        doNothing().when(service).updateResetPasswordToken(anyString(), eq(request.email()));

        mockMvc.perform(post("/api/v1/recovery/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("We have sent a reset password link to your email. Please check."));
    }

    @Test
    void testProcessResetPassword_UserFound() throws Exception {
        String token = "validToken";
        String newPassword = "newPassword";
        ResetPasswordRequest request = new ResetPasswordRequest(token, newPassword);
        User user = new User();
        user.setEmail("test@example.com");

        when(service.getByResetPasswordToken(token)).thenReturn(user);
        doNothing().when(service).updateRecoveryPassword(user, newPassword);

        mockMvc.perform(post("/api/v1/recovery/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("You have successfully changed your password."));
    }

    @Test
    void testProcessResetPassword_UserNotFound() throws Exception {
        String token = "invalidToken";
        String newPassword = "newPassword";
        ResetPasswordRequest request = new ResetPasswordRequest(token, newPassword);

        when(service.getByResetPasswordToken(token)).thenReturn(null);

        mockMvc.perform(post("/api/v1/recovery/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Token"));
    }
}
