package tech.engix.auth_service.dto;

public record UserRecoveryPassword(
        String currentPassword,
        String newPassword,
        String confirmPassword) {
}
