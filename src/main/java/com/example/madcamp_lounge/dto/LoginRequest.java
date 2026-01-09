package com.example.madcamp_lounge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "loginId is required")
    private String loginId;

    @NotBlank(message = "password is required")
    private String password;
}
