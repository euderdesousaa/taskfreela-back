package tech.engix.auth_service.security.oauth2.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.engix.auth_service.security.services.CustomUserDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthResponse {
    private String jwt;
    private CustomUserDetail userDetail;
}
