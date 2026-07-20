package com.ajith.KnowTheRound.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Password Reset Request");

        message.setText("""
                Hello,

                You requested a password reset.

                Use the following token to reset your password:

                %s

                This token is valid for 30 minutes.

                If you did not request this, please ignore this email.
                """.formatted(token));

        mailSender.send(message);
    }

    public void sendVerificationEmail(String to, String name, String token) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Verify Your Email");

        String verificationLink =
                "http://localhost:8080/api/auth/verify-email?token=" + token;

        message.setText("""
            Hello %s,

            Welcome to KnowTheRound!

            Please verify your email by clicking the link below:

            %s

            This link will expire in 24 hours.

            If you did not create this account, you can safely ignore this email.

            Regards,
            KnowTheRound Team
            """.formatted(name, verificationLink));

        mailSender.send(message);
    }

}