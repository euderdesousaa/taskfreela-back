package tech.engix.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @Email
        String username,
        @NotEmpty
        String password
) {
}
