package tech.engix.auth_service.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest(
        @NotEmpty
        String currentPassword,
        @NotEmpty
        String newPassword,
        @NotEmpty
        String confirmPassword) {
}
