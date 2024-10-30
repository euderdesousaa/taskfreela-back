package tech.engix.auth_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordMessage {
    private String email;
    private String resetPasswordLink;
}
