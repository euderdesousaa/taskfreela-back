package tech.engix.auth_service.security.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import tech.engix.auth_service.controller.exception.exceptions.CustomerNotFoundException;
import tech.engix.auth_service.security.jwt.service.RefreshTokenService;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Service
public class CustomJWTLogoutHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;

    @Value("${tech.engix.jwtSecret}")
    private String secretKey;

    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie jwtCookie = new Cookie("accessToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        String username = getUsernameFromRequest(request);
        if (username != null) {
            refreshTokenService.deleteRefreshToken(username);
            Cookie refreshToken = new Cookie("refreshToken", null);
            refreshToken.setHttpOnly(true);
            refreshToken.setPath("/");
            refreshToken.setMaxAge(0);
            response.addCookie(refreshToken);

        } else {
            throw new CustomerNotFoundException("Nenhum nome de usuário encontrado na requisição para logout.");
        }
    }

    private String getUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            return extractUsernameFromToken(jwtToken);
        }
        return null;
    }

    private String extractUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}


