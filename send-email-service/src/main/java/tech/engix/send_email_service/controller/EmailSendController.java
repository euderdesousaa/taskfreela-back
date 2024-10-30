package tech.engix.send_email_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import tech.engix.send_email_service.dto.ResetPasswordMessage;
import tech.engix.send_email_service.service.EmailSendService;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailSendController {

    private final EmailSendService service;

    @KafkaListener(topics = "auth-reset", groupId = "auth-group")
    public void sendResetPassword(String msgJson) throws MessagingException, UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResetPasswordMessage message = objectMapper.readValue(msgJson, ResetPasswordMessage.class);

        String recipientEmail = message.getEmail();
        String link = message.getResetPasswordLink();

        service.sendResetPassword(recipientEmail, link);
    }

    @KafkaListener(topics = "auth-welcome", groupId = "auth-group")
    public ResponseEntity<Void> sendWelcome(@RequestParam("recipientEmail") String recipientEmail)
            throws MessagingException, UnsupportedEncodingException {
        service.sendWelcomeMail(recipientEmail);
        return ResponseEntity.ok().build();
    }
}
