package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.dto.AuthResponse;
import com.example.madcamp_lounge.dto.LoginRequest;
import com.example.madcamp_lounge.entity.RefreshToken;
import com.example.madcamp_lounge.entity.User;
import com.example.madcamp_lounge.repository.RefreshTokenRepository;
import com.example.madcamp_lounge.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Optional<AuthResponse> login(LoginRequest request) {
        Optional<User> user = userService.login(request);
        if (user.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(issueTokens(user.get()));
    }

    @Transactional
    public Optional<AuthResponse> refresh(String refreshToken) {
        try {
            Claims claims = jwtTokenProvider.parseClaims(refreshToken);
            String type = claims.get("type", String.class);
            if (!"refresh".equals(type)) {
                return Optional.empty();
            }

            Optional<RefreshToken> stored = refreshTokenRepository.findByToken(refreshToken);
            if (stored.isEmpty()) {
                return Optional.empty();
            }

            Long userId = Long.parseLong(claims.getSubject());
            Optional<User> user = userService.getLoginUserById(userId);
            if (user.isEmpty()) {
                return Optional.empty();
            }

            refreshTokenRepository.deleteByUserId(userId);
            return Optional.of(issueTokens(user.get()));
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        LocalDateTime refreshExpiresAt = LocalDateTime.now()
            .plusSeconds(jwtTokenProvider.getRefreshTokenTtlSeconds());

        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken, refreshExpiresAt));

        return AuthResponse.from(user, accessToken, refreshToken);
    }
}
