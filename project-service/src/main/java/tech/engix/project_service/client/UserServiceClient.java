package tech.engix.project_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.engix.project_service.dto.UserResponse;

@FeignClient(url = "localhost:8081", name = "auth-service")
public interface UserServiceClient {
    @GetMapping("/api/v1/accounts/by-email")
    UserResponse getUserByEmail(@RequestParam("email") String email);
}
