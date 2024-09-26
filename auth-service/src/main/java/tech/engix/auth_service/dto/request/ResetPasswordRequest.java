package tech.engix.auth_service.dto.request;

public record ResetPasswordRequest(String token,
                                   String password) {
}
