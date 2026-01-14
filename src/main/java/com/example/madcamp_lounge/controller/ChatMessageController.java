package com.example.madcamp_lounge.controller;

import com.example.madcamp_lounge.dto.ChatMessageRequest;
import com.example.madcamp_lounge.dto.ChatMessageResponse;
import com.example.madcamp_lounge.dto.ChatRoomEventResponse;
import com.example.madcamp_lounge.dto.ChatReadRequest;
import com.example.madcamp_lounge.dto.ChatReadResponse;
import com.example.madcamp_lounge.entity.Message;
import com.example.madcamp_lounge.repository.ChatRoomMemberRepository;
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
    private final ChatRoomMemberRepository chatRoomMemberRepository;

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
        // Mark sender as read for their own message so unread count excludes the sender.
        chatMessageService.markRead(roomId, userId, saved.getId());
        long totalMembers = chatRoomMemberRepository.countByRoomId(roomId);
        long readCount = chatRoomMemberRepository
            .countByRoomIdAndLastReadMessageIdGreaterThanEqual(roomId, saved.getId());
        long unreadCount = Math.max(0, totalMembers - readCount);
        ChatMessageResponse response = ChatMessageResponse.from(saved, unreadCount);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, response);
        messagingTemplate.convertAndSend(
            "/topic/rooms",
            new ChatRoomEventResponse(
                roomId,
                userId,
                saved.getContent(),
                saved.getSentAt()
            )
        );
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
