package tech.engix.auth_service.dto;

public record TokenRefreshResponse(String newAccessToken, String newRefreshToken) {
}
