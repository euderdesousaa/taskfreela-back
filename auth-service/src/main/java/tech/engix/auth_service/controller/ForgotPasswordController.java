package tech.engix.auth_service.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tech.engix.auth_service.controller.exception.exceptions.CustomerNotFoundException;
import tech.engix.auth_service.dto.request.ForgotPasswordRequest;
import tech.engix.auth_service.dto.request.ResetPasswordRequest;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.service.UserService;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

@RestController
@RequestMapping("/api/v1/recovery")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ForgotPasswordController {

    private final JavaMailSender mailSender;
    @Value("${tech.engix.url}")
    private String url;
    private final UserService service;

    private final SpringTemplateEngine templateEngine;

    private final SecureRandom secureRandom = new SecureRandom();

    @PostMapping("/forgot_password")
    public ResponseEntity<String> processForgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.email();
        String token = generateString(secureRandom);

        try {
            service.updateResetPasswordToken(token, email);
            String resetPasswordLink = url + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            return ResponseEntity.ok("We have sent a reset password link to your email. Please check.");
        } catch (CustomerNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            return ResponseEntity.status(500).body("Error while sending email");
        }
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("fishying14@gmail.com", "Engix Tech");
        helper.setTo(recipientEmail);
        helper.setSubject("Here's the link to reset your password");

        Context context = new Context();
        context.setVariable("link", link);

        String htmlContent = templateEngine.process("forgot-password", context);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<String> processResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        String token = request.token();
        String password = request.password();

        User customer = service.getByResetPasswordToken(token);

        if (customer == null) {
            return ResponseEntity.badRequest().body("Invalid Token");
        } else {
            service.updateRecoveryPassword(customer, password);
            return ResponseEntity.ok("You have successfully changed your password.");
        }
    }

    private static String generateString(SecureRandom secureRandom) {
        String fullCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char[] text = new char[128];
        for (int i = 0; i < 128; i++) {
            text[i] = fullCharacters.charAt(secureRandom.nextInt(fullCharacters.length()));
        }
        return new String(text);
    }


}