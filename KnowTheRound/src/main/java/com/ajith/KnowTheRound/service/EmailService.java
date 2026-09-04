package com.ajith.KnowTheRound.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.api-key}")
    private String brevoApiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    @Value("${brevo.sender-name}")
    private String senderName;

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.brevo.com/v3")
            .build();

    public void sendPasswordResetEmail(String to, String token) {

        String resetLink =
                "https://knowtheround.vercel.app/reset-password?token=" + token;

        String htmlContent = """
                <html>
                <body>
                    <h2>Reset Your KnowTheRound Password</h2>

                    <p>Hello,</p>

                    <p>
                        We received a request to reset your KnowTheRound
                        account password.
                    </p>

                    <p>
                        <a href="%s">
                            Click here to reset your password
                        </a>
                    </p>

                    <p>This link is valid for 30 minutes.</p>

                    <p>
                        If you did not request a password reset,
                        you can safely ignore this email.
                    </p>

                    <p>
                        Regards,<br>
                        KnowTheRound Team
                    </p>
                </body>
                </html>
                """.formatted(resetLink);

        sendEmail(
                to,
                null,
                "Reset Your KnowTheRound Password",
                htmlContent
        );
    }

    public void sendVerificationEmail(String to, String name, String token) {

        String verificationLink =
                "https://knowtheround-e767.onrender.com/api/auth/verify-email?token=" + token;

        String htmlContent = """
                <html>
                <body>
                    <h2>Welcome to KnowTheRound!</h2>

                    <p>Hello %s,</p>

                    <p>
                        Thank you for registering with KnowTheRound.
                    </p>

                    <p>
                        <a href="%s">
                            Click here to verify your email
                        </a>
                    </p>

                    <p>This link will expire in 24 hours.</p>

                    <p>
                        If you did not create this account,
                        you can safely ignore this email.
                    </p>

                    <p>
                        Regards,<br>
                        KnowTheRound Team
                    </p>
                </body>
                </html>
                """.formatted(name, verificationLink);

        sendEmail(
                to,
                name,
                "Verify Your KnowTheRound Email",
                htmlContent
        );
    }

    private void sendEmail(
            String to,
            String recipientName,
            String subject,
            String htmlContent
    ) {

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", senderName,
                        "email", senderEmail
                ),
                "to", List.of(
                        recipientName == null
                                ? Map.of("email", to)
                                : Map.of(
                                "email", to,
                                "name", recipientName
                        )
                ),
                "subject", subject,
                "htmlContent", htmlContent
        );

        restClient.post()
                .uri("/smtp/email")
                .header("api-key", brevoApiKey)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
