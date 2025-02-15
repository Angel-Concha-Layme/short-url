package dev.shorturl.security.service;

import dev.shorturl.security.exception.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verify Your Email Address";

        Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("verificationCode", verificationCode);

        String body = templateEngine.process("email/verification-email", context);
        sendHtmlEmail(toEmail, subject, body);
    }

    public void sendWelcomeEmail(String toEmail, String firstName) {
        String subject = "Welcome to " + appName;

        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("appName", appName);

        String body = templateEngine.process("email/welcome-email", context);
        sendHtmlEmail(toEmail, subject, body);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send email to: " + to, e);
        }
        mailSender.send(message);
    }
}