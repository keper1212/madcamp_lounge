package com.example.madcamp_lounge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String password;
    private String nickname;
    private String hobby;
    private String introduction;
}
