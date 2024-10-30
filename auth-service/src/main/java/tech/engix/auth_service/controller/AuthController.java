package tech.engix.auth_service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.engix.auth_service.dto.LoginDto;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.dto.responses.LoginResponseDTO;
import tech.engix.auth_service.dto.responses.UserResponseDTO;
import tech.engix.auth_service.security.jwt.JwtUtils;
import tech.engix.auth_service.security.jwt.service.RefreshTokenService;
import tech.engix.auth_service.service.AuthService;
import tech.engix.auth_service.service.UserService;
import tech.engix.auth_service.util.CookieUtils;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Authentication", description = "All fields for login and register")
public class AuthController {

    private final AuthService service;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${tech.engix.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value(value = "${tech.engix.jwtRefreshToken}")
    private int refreshJwtExpirationMs;


    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody SignUpDto dto) {
        UserResponseDTO sign = service.registerUser(dto);
        kafkaTemplate.send("auth-welcome", sign.email());
        return ResponseEntity.ok(sign);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginDto loginDto,
                                              HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);

            refreshTokenService.saveRefreshToken(loginDto.username(), refreshToken);

            CookieUtils.addCookie(response, "accessToken", accessToken, jwtExpirationMs);
            CookieUtils.addCookie(response, "refreshToken", refreshToken, refreshJwtExpirationMs);

            String name = userService.getUserNameByUsername(loginDto.username());

            return ResponseEntity.ok().body(new LoginResponseDTO(accessToken, refreshToken, name));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied: " + e.getMessage());
        }
    }

}


