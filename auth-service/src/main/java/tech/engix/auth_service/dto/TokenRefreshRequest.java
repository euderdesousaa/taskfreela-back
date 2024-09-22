package tech.engix.auth_service.dto;

public record TokenRefreshRequest(
        String refreshToken
) {
}
