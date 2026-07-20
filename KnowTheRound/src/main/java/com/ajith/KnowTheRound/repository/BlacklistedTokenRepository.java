package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);
}