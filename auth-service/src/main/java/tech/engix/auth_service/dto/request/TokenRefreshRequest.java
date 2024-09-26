package tech.engix.auth_service.dto.request;

public record TokenRefreshRequest(
        String refreshToken
) {
}
