package tech.engix.auth_service.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import tech.engix.auth_service.dto.request.ChangePasswordRequest;
import tech.engix.auth_service.dto.responses.UserClientResponse;
import tech.engix.auth_service.dto.user.UserUpdateDTO;
import tech.engix.auth_service.service.UserService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "User Settings", description = "All User Settings")
public class UserController {

    private final UserService service;

    @PutMapping("/edit")
    public ResponseEntity<UserUpdateDTO> updateDto(Principal principal,
                                                   @RequestBody UserUpdateDTO dto) {
        try {
            if(principal == null) {
                throw new AccessDeniedException("Unauthorized");
            }
            service.updateUser(principal.getName(), dto);
            return ResponseEntity.ok().body(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<Object> updatePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest dto) {
        try {
            if (!dto.newPassword().equals(dto.confirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error", "New passwords do not match"));
            }

            service.updatePassword(principal.getName(), dto);

            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (Exception e) {
            throw new AccessDeniedException("Password update failed: unauthorized access.");
        }
    }

    @Hidden
    @GetMapping("/by-email")
    public ResponseEntity<UserClientResponse> getUserByEmail(@RequestParam("email") String email) {
        UserClientResponse user = service.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
