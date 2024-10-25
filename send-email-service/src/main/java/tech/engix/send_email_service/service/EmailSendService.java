package tech.engix.send_email_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailSendService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public void sendResetPassword(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
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

    public void sendWelcomeMail(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("fishying14@gmail.com", "Engix Tech");
        helper.setTo(recipientEmail);
        helper.setSubject("Bem-vindo(a) à TaskFreela – Comece a gerenciar seus projetos agora!");

        Context context = new Context();

        String htmlContent = templateEngine.process("welcome", context);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
