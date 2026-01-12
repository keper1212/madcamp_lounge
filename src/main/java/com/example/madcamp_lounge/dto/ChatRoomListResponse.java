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
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public static ChatRoomListResponse from(ChatRoom room) {
        return new ChatRoomListResponse(
            room.getId(),
            room.getPartyId(),
            room.getCreatedAt()
        );
    }
}
