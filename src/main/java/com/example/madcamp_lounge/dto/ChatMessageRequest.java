package com.example.madcamp_lounge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatMessageRequest {
    @NotBlank(message = "content is required")
    private String content;
}
