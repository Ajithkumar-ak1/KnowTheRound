package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.auth.*;
import com.ajith.KnowTheRound.enums.Role;
import com.ajith.KnowTheRound.exception.BadRequestException;
import com.ajith.KnowTheRound.exception.DuplicateResourceException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.*;
import com.ajith.KnowTheRound.repository.BlacklistedTokenRepository;
import com.ajith.KnowTheRound.repository.EmailVerificationTokenRepository;
import com.ajith.KnowTheRound.repository.PasswordResetTokenRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
import com.ajith.KnowTheRound.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthResponseDto register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();

        User savedUser = userRepository.save(user);

        EmailVerificationToken verificationToken =
                createVerificationToken(savedUser);

        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getName(),
                verificationToken.getToken()
        );

        return AuthResponseDto.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .message("Registration successful. Please verify your email.")
                .build();
    }

    public AuthResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException(
                    "Please verify your email before logging in."
            );
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        // Prevent email enumeration
        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();

        PasswordResetToken resetToken = createPasswordResetToken(user);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(request.getToken())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Invalid password reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new BadRequestException("Password reset token expired");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        // Revoke all refresh tokens so existing sessions are logged out
        refreshTokenService.deleteRefreshToken(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    public void logout(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if (!blacklistedTokenRepository.existsByToken(token)) {

            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .token(token)
                    .expiryDate(jwtService.extractExpiry(token))
                    .build();

            blacklistedTokenRepository.save(blacklistedToken);
        }

        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        refreshTokenService.deleteRefreshToken(user);
    }

    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequest request) {

        RefreshToken oldRefreshToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken()
                );

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(
                        oldRefreshToken.getUser()
                );

        String accessToken =
                jwtService.generateToken(oldRefreshToken.getUser());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .type("Bearer")
                .userId(oldRefreshToken.getUser().getId())
                .name(oldRefreshToken.getUser().getName())
                .email(oldRefreshToken.getUser().getEmail())
                .role(oldRefreshToken.getUser().getRole().name())
                .build();
    }

    @Transactional
    private EmailVerificationToken createVerificationToken(User user) {

        emailVerificationTokenRepository.deleteByUser(user);

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        return emailVerificationTokenRepository.save(verificationToken);
    }

    @Transactional
    public void verifyEmail(String token) {

        EmailVerificationToken verificationToken =
                emailVerificationTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Invalid verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            emailVerificationTokenRepository.delete(verificationToken);
            throw new BadRequestException("Verification token expired");
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        emailVerificationTokenRepository.delete(verificationToken);
    }

    @Transactional
    private PasswordResetToken createPasswordResetToken(User user) {

        passwordResetTokenRepository.deleteByUser(user);

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        return passwordResetTokenRepository.save(resetToken);
    }
}