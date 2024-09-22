package tech.engix.auth_service.dto.user;

import lombok.Builder;

@Builder
public record UserUpdateDTO(
        String name,
        String email
) {
}
