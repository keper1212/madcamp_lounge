package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatRoomCreateRequest {
    @NotNull(message = "user_id is required")
    @JsonProperty("user_id")
    private Long userId;
}
