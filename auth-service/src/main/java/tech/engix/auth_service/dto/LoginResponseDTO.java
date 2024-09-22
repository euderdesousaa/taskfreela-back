package tech.engix.auth_service.dto;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String jwt,
                               String refreshToken) {
}
