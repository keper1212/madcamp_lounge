package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomEventResponse {
    @JsonProperty("room_id")
    private Long roomId;
    @JsonProperty("sender_id")
    private Long senderId;
    private String content;
    @JsonProperty("sent_at")
    private LocalDateTime sentAt;
}
