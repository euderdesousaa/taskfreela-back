package tech.engix.auth_service.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
import tech.engix.auth_service.controller.exception.exceptions.CustomerNotFoundException;
import tech.engix.auth_service.dto.ForgotPasswordRequest;
import tech.engix.auth_service.dto.ResetPasswordRequest;
import tech.engix.auth_service.model.User;
import tech.engix.auth_service.service.UserService;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/recovery")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ForgotPasswordController {

    private final JavaMailSender mailSender;
    @Value("${tech.engix.url}")
    private String url;
    private final UserService service;
    private final Random rng = new Random();

    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.email();
        String token = generateString(rng, "abcdef", 8); // Considere adicionar mais caracteres

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

        helper.setFrom("contact@shopme.com", "Shopme Support");
        helper.setTo(recipientEmail);
        helper.setSubject("Here's the link to reset your password");

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, or you have not made the request.</p>";

        helper.setText(content, true);
        mailSender.send(message);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> processResetPassword(@RequestBody ResetPasswordRequest request) {
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

    private static String generateString(Random rng, String characters, int length) {
        String fullCharacters = characters + "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = fullCharacters.charAt(rng.nextInt(fullCharacters.length()));
        }
        return new String(text);
    }

}
