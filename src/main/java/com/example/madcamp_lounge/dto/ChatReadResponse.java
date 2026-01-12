package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatReadResponse {
    @JsonProperty("room_id")
    private Long roomId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("last_message_id")
    private Long lastMessageId;
    @JsonProperty("read_at")
    private LocalDateTime readAt;
}
