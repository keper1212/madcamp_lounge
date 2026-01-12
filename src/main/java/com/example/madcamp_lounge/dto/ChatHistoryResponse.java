package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatHistoryResponse {
    private List<ChatMessageResponse> messages;
    @JsonProperty("next_cursor")
    private LocalDateTime nextCursor;
    @JsonProperty("next_cursor_id")
    private Long nextCursorId;
    @JsonProperty("has_more")
    private boolean hasMore;
}
