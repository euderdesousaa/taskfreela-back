package tech.engix.auth_service.dto.request;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmPassword) {
}
