package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.EmailVerificationToken;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);
}