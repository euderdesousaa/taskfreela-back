package tech.engix.auth_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.engix.auth_service.dto.ResetPasswordMessage;
import tech.engix.auth_service.dto.request.ForgotPasswordRequest;
import tech.engix.auth_service.dto.request.ResetPasswordRequest;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.service.ForgotPasswordService;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/v1/recovery")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Forgot Password", description = "User Recovery Password")
public class ForgotPasswordController {

    @Value("${tech.engix.url}")
    private String url;

    private final ForgotPasswordService service;

    private final SecureRandom secureRandom = new SecureRandom();

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> processForgotPassword(@RequestBody ForgotPasswordRequest request) throws JsonProcessingException {
        String email = request.email();
        String token = generateString(secureRandom);
        service.updateResetPasswordToken(token, email);

        String resetPasswordLink = url + "/reset_password?token=" + token;

        ResetPasswordMessage message = new ResetPasswordMessage();
        message.setEmail(email);
        message.setResetPasswordLink(resetPasswordLink);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(message);

        kafkaTemplate.send("auth-reset", json);

        return ResponseEntity.ok("We have sent a reset password link to your email. Please check.");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> processResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        String token = request.token();
        String password = request.password();

        User customer = service.getByResetPasswordToken(token);

        if (customer == null) {
            return ResponseEntity.badRequest().body("Invalid Token");
        } else {
            service.updateRecoveryPassword(customer, password);
            return ResponseEntity.ok("You have successfully changed your password.");
        }
    }

    private static String generateString(SecureRandom secureRandom) {
        String fullCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char[] text = new char[128];
        for (int i = 0; i < 128; i++) {
            text[i] = fullCharacters.charAt(secureRandom.nextInt(fullCharacters.length()));
        }
        return new String(text);
    }


}