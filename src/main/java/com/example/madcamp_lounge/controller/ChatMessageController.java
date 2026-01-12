package com.example.madcamp_lounge.controller;

import com.example.madcamp_lounge.dto.ChatMessageRequest;
import com.example.madcamp_lounge.dto.ChatMessageResponse;
import com.example.madcamp_lounge.dto.ChatReadRequest;
import com.example.madcamp_lounge.dto.ChatReadResponse;
import com.example.madcamp_lounge.entity.Message;
import com.example.madcamp_lounge.service.ChatMessageService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/rooms/{roomId}")
    public void sendMessage(
        @DestinationVariable Long roomId,
        @Valid ChatMessageRequest request,
        Principal principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("missing principal");
        }

        Long userId;
        try {
            userId = Long.parseLong(principal.getName());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("invalid principal");
        }

        Message saved = chatMessageService.saveMessage(roomId, userId, request.getContent());
        ChatMessageResponse response = ChatMessageResponse.from(saved);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, response);
    }

    @MessageMapping("/rooms/{roomId}/read")
    public void markRead(
        @DestinationVariable Long roomId,
        @Valid ChatReadRequest request,
        Principal principal
    ) {
        if (principal == null) {
            throw new IllegalStateException("missing principal");
        }

        Long userId;
        try {
            userId = Long.parseLong(principal.getName());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("invalid principal");
        }

        LocalDateTime readAt = chatMessageService.markRead(
            roomId,
            userId,
            request.getLastMessageId()
        );
        ChatReadResponse response = new ChatReadResponse(
            roomId,
            userId,
            request.getLastMessageId(),
            readAt
        );
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId + "/read", response);
    }
}
