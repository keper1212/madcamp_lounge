package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String loginId;
    private String nickname;
    private Boolean isFirstLogin;

    public static AuthResponse from(User user, String accessToken, String refreshToken) {
        return new AuthResponse(
            accessToken,
            refreshToken,
            user.getId(),
            user.getLoginId(),
            user.getNickname(),
            user.getIsFirstLogin()
        );
    }
}
