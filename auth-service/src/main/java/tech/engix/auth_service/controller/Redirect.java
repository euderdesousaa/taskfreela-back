package tech.engix.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import tech.engix.auth_service.security.jwt.JwtUtils;
import tech.engix.auth_service.security.oauth2.CustomOAuth2UserService;
import tech.engix.auth_service.security.oauth2.user.OAuthResponse;
import tech.engix.auth_service.security.services.CustomUserDetail;

@RestController
@RequiredArgsConstructor
public class Redirect {

    private final JwtUtils jwtUtils;
    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(@RequestParam String token) {
        if (jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.ok("Bem-vindo! Seu token é válido.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
        }
    }

    @GetMapping("/oauth2/callback/{registrationId}")
    public ResponseEntity<?> handleOAuth2Callback(
            @PathVariable String registrationId,
            OAuth2AuthenticationToken authentication) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication is null");
        }

        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new OAuthResponse(jwtToken, userDetail));
    }

    @PostMapping("/oauth2/callback/{provider}")
    public ResponseEntity<OAuth2User> oauth2Callback(
            @PathVariable String provider,
            OAuth2UserRequest userRequest) {
        OAuth2User response = customOAuth2UserService.loadUser(userRequest);
        return ResponseEntity.ok(response);
    }



}