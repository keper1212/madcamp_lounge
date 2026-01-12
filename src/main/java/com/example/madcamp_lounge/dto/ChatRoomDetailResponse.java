package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomDetailResponse {
    private ChatRoomInfo room;
    private List<PartyMemberResponse> members;
    private List<ChatMessageResponse> messages;
    @JsonProperty("next_cursor")
    private LocalDateTime nextCursor;
    @JsonProperty("has_more")
    private boolean hasMore;

    @Getter
    @AllArgsConstructor
    public static class ChatRoomInfo {
        @JsonProperty("room_id")
        private Long roomId;
        @JsonProperty("party_id")
        private Long partyId;
        @JsonProperty("party_title")
        private String partyTitle;
        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }
}
