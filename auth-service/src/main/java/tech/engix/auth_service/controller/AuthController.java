package tech.engix.auth_service.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.engix.auth_service.dto.*;
import tech.engix.auth_service.security.jwt.JwtUtils;
import tech.engix.auth_service.security.jwt.service.RefreshTokenService;
import tech.engix.auth_service.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final AuthService service;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody SignUpDto dto) {
        UserResponseDTO sign = service.registerUser(dto);
        return ResponseEntity.ok(sign);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.generateJwtToken(authentication);

            String refreshToken = jwtUtils.generateRefreshToken(authentication);

            refreshTokenService.saveRefreshToken(loginDto.username(), refreshToken);

            return ResponseEntity.ok().body(new LoginResponseDTO(accessToken, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);

        String storedRefreshToken = refreshTokenService.getRefreshToken(username);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken) || !jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.status(403).body("Invalid Refresh Token");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = jwtUtils.generateJwtToken(authentication);
        String newRefreshToken = jwtUtils.generateRefreshToken(authentication); // Gere um novo refresh token

        refreshTokenService.saveRefreshToken(username, newRefreshToken);

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefreshToken)); // Retorne o novo refresh token também
    }


}


