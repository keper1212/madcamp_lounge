package com.example.madcamp_lounge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequest {
    @NotBlank(message = "new_pw is required")
    private String newPw;
}
