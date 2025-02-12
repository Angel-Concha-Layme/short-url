package dev.shorturl.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verify Your Email Address";
        String body = buildVerificationEmailBody(verificationCode);
        sendEmail(toEmail, subject, body);
    }

    public void sendWelcomeEmail(String toEmail, String firstName) {
        String subject = "Welcome to " + appName;
        String body = buildWelcomeEmailBody(firstName);
        sendEmail(toEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    private String buildVerificationEmailBody(String verificationCode) {
        return String.format("""
            Welcome to %s!

            Your verification code is: %s

            If you didn't request this code, you can safely ignore this email.

            Best regards,
            The %s Team
            """, appName, verificationCode, appName);
    }

    private String buildWelcomeEmailBody(String firstName) {
        return String.format("""
            Hi %s,

            Welcome to %s! We're excited to have you on board.

            You can now log in to your account and start using our services.

            If you have any questions, feel free to reply to this email.

            Best regards,
            The %s Team
            """, firstName, appName, appName);
    }
}