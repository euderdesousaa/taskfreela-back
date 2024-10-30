package tech.engix.auth_service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.engix.auth_service.dto.request.TokenRefreshRequest;
import tech.engix.auth_service.dto.responses.TokenRefreshResponse;
import tech.engix.auth_service.security.jwt.JwtUtils;
import tech.engix.auth_service.security.jwt.service.RefreshTokenService;
import tech.engix.auth_service.util.CookieUtils;

@RestController
@RequestMapping("/api/v1/refresh")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Refresh Token", description = "All fields for login and register")
public class RefreshTokenController {

    @Value(value = "${tech.engix.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value(value = "${tech.engix.jwtRefreshToken}")
    private int refreshJwtExpirationMs;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @PostMapping
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request,
                                     HttpServletResponse response) {
        String refreshToken = request.refreshToken();
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);

        String storedRefreshToken = refreshTokenService.getRefreshToken(username);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken) || !jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.status(403).body("Invalid Refresh Token");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = jwtUtils.generateJwtToken(authentication);
        String newRefreshToken = jwtUtils.generateRefreshToken(authentication);

        CookieUtils.addCookie(response, "accessToken", newAccessToken, jwtExpirationMs);
        CookieUtils.addCookie(response, "refreshToken", newRefreshToken, refreshJwtExpirationMs);

        refreshTokenService.saveRefreshToken(username, newRefreshToken);

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefreshToken));
    }
}
