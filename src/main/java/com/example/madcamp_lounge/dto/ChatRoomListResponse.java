package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomListResponse {
    @JsonProperty("room_id")
    private Long roomId;
    @JsonProperty("party_id")
    private Long partyId;
    @JsonProperty("party_title")
    private String partyTitle;
    @JsonProperty("other_name")
    private String otherName;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("last_message_at")
    private LocalDateTime lastMessageAt;
    @JsonProperty("last_message_content")
    private String lastMessageContent;
    @JsonProperty("unread_count")
    private long unreadCount;

    public static ChatRoomListResponse from(
        ChatRoom room,
        String partyTitle,
        String otherName,
        LocalDateTime lastMessageAt,
        String lastMessageContent,
        long unreadCount
    ) {
        return new ChatRoomListResponse(
            room.getId(),
            room.getPartyId(),
            partyTitle,
            otherName,
            room.getCreatedAt(),
            lastMessageAt,
            lastMessageContent,
            unreadCount
        );
    }
}
