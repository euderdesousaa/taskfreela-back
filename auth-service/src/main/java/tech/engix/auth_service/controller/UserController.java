package tech.engix.auth_service.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.engix.auth_service.dto.UserRecoveryPassword;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.service.UserService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService service;

    @Hidden
    @GetMapping
    public String hello(Principal principal) {
        return "Hello " + principal.getName();
    }

    @PutMapping("/edit")
    public ResponseEntity<UserUpdateDTO> updateDto(Principal principal,
                                                   @RequestBody UserUpdateDTO dto) {
        service.updateUser(principal.getName(), dto);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(Principal principal, @RequestBody UserRecoveryPassword dto) {
        try {
            if (!dto.newPassword().equals(dto.confirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error", "New passwords do not match"));
            }

            service.updatePassword(principal.getName(), dto);

            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
