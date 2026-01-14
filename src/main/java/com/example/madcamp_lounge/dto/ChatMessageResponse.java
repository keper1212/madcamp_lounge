package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {
    @JsonProperty("message_id")
    private Long messageId;
    @JsonProperty("sender_id")
    private Long senderId;
    private String content;
    @JsonProperty("sent_at")
    private LocalDateTime sentAt;
    @JsonProperty("unread_count")
    private long unreadCount;

    public static ChatMessageResponse from(Message message, long unreadCount) {
        return new ChatMessageResponse(
            message.getId(),
            message.getSenderId(),
            message.getContent(),
            message.getSentAt(),
            unreadCount
        );
    }
}
