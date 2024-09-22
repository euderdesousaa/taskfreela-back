package tech.engix.auth_service.dto;

public record ChangePassword(
        String currentPassword,
        String newPassword,
        String confirmPassword) {
}
