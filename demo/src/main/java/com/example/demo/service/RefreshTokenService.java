package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.RefreshToken;

import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import java.time.Instant;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(345600)) // 1 mes de token
                .build();

        return refreshTokenRepository.save(refreshToken);

    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByuserId(int userid) {
        return refreshTokenRepository.findByUser_id(userid);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Token Expired, please sign in again");
        }

        return token;

    }

}
