package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomCreateResponse {
    @JsonProperty("room_id")
    private Long roomId;
    @JsonProperty("party_id")
    private Long partyId;

    public static ChatRoomCreateResponse from(ChatRoom room) {
        return new ChatRoomCreateResponse(room.getId(), room.getPartyId());
    }
}
