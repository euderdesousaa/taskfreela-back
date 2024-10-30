package tech.engix.send_email_service.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.engix.send_email_service.service.EmailSendService;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailSendController {

    private final EmailSendService service;

    @PostMapping("/send-reset-email")
    @KafkaListener(topics = "auth-reset", groupId = "auth-group")
    public ResponseEntity<Void> sendResetPassword(@RequestParam("recipientEmail") String recipientEmail,
                                                  @RequestParam String link) throws MessagingException, UnsupportedEncodingException {
        service.sendResetPassword(recipientEmail, link);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-welcome-email")
    @KafkaListener(topics = "auth-welcome", groupId = "auth-group")
    public ResponseEntity<Void> sendWelcome(@RequestParam("recipientEmail") String recipientEmail)
            throws MessagingException, UnsupportedEncodingException {
        service.sendWelcomeMail(recipientEmail);
        return ResponseEntity.ok().build();
    }
}
