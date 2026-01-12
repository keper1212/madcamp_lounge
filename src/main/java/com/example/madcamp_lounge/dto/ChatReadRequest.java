package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatReadRequest {
    @NotNull(message = "last_message_id is required")
    @JsonProperty("last_message_id")
    private Long lastMessageId;
}
