package tech.engix.auth_service.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UserResponseDTO(
        String name,
        @Email(message = "This email is invalid or used")
        String email
)
{}
