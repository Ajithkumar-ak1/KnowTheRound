package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.RefreshToken;
import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        refreshTokenRepository.flush();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        return refreshToken;
    }

    @Transactional
    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }


}