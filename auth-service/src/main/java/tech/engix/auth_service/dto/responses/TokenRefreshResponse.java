package tech.engix.auth_service.dto.responses;

public record TokenRefreshResponse(String newAccessToken, String newRefreshToken) {
}
