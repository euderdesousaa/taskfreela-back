package tech.engix.auth_service.dto;

import jakarta.validation.constraints.Email;

public record UserResponseDTO(
        String name,
        @Email(message = "This email is invalid or used")
        String email
)
{}
