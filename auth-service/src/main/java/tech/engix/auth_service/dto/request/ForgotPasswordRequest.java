package tech.engix.auth_service.dto.request;

import jakarta.validation.constraints.Email;

public record ForgotPasswordRequest(
        @Email
        String email) {
}
