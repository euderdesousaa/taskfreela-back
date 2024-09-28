package tech.engix.auth_service.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ResetPasswordRequest(String token,
                                   @NotEmpty
                                   String password) {
}
