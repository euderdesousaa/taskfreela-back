package tech.engix.auth_service.dto.responses;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public record UserClientResponse(
        String name,
        String email,
        Long id
) {
}
