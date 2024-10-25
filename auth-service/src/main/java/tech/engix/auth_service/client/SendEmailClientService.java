package tech.engix.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "localhost:8085", name = "email-service")
public interface SendEmailClientService {

    @PostMapping("/api/v1/email/send-reset-email")
    void sendResetPassword(@RequestParam("recipientEmail") String recipientEmail,
                           @RequestParam String link);

    @PostMapping("/api/v1/email/send-welcome-email")
    void sendWelcomeEmail(@RequestParam("recipientEmail") String recipientEmail);
}
