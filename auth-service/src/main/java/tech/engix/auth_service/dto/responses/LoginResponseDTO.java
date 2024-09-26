package tech.engix.auth_service.dto.responses;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String jwt,
                               String refreshToken,
                               String name) {
}
