package tech.engix.auth_service.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Email;

@Hidden
public record ForgotPasswordRequest(
        @Email
        String email) {
}
