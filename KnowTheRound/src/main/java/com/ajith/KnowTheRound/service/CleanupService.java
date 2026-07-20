package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.BlacklistedTokenRepository;
import com.ajith.KnowTheRound.repository.EmailVerificationTokenRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void cleanupExpiredData() {

        LocalDateTime now = LocalDateTime.now();

        // Delete expired blacklisted tokens
        blacklistedTokenRepository.deleteByExpiryDateBefore(now);

        // Clear expired password reset tokens
        List<User> users = userRepository.findByPasswordResetTokenExpiryBefore(now);

        for (User user : users) {
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiry(null);
        }

        userRepository.saveAll(users);

        log.info("Cleanup completed. Cleared {} expired reset tokens.", users.size());
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredVerificationTokens() {

        emailVerificationTokenRepository
                .deleteByExpiryDateBefore(LocalDateTime.now());

        System.out.println("Token cleanup executed: " + LocalDateTime.now());
    }
}