package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String loginId;
    private String name;
    private String nickname;
    private String mbti;
    private String classSection;
    private String hobby;
    private String introduction;
    private Boolean isFirstLogin;

    public static LoginResponse from(User user) {
        return new LoginResponse(
            user.getId(),
            user.getLoginId(),
            user.getName(),
            user.getNickname(),
            user.getMbti(),
            user.getClassSection(),
            user.getHobby(),
            user.getIntroduction(),
            user.getIsFirstLogin()
        );
    }
}
