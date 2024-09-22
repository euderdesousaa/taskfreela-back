package tech.engix.auth_service.dto;

public record ResetPasswordRequest(String token,
                                   String password) {
}
