package com.example.madcamp_lounge.security;

import com.example.madcamp_lounge.repository.ChatRoomMemberRepository;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSubscriptionInterceptor implements ChannelInterceptor {
    private static final Pattern ROOM_DESTINATION =
        Pattern.compile("^/topic/rooms/(?<roomId>\\d+)$");

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public WebSocketSubscriptionInterceptor(ChatRoomMemberRepository chatRoomMemberRepository) {
        this.chatRoomMemberRepository = chatRoomMemberRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination == null) {
                throw new IllegalArgumentException("missing destination");
            }

            Matcher matcher = ROOM_DESTINATION.matcher(destination);
            if (!matcher.matches()) {
                return message;
            }

            Principal principal = accessor.getUser();
            if (principal == null) {
                throw new IllegalArgumentException("missing principal");
            }

            Long userId;
            try {
                userId = Long.parseLong(principal.getName());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("invalid principal");
            }

            Long roomId = Long.parseLong(matcher.group("roomId"));
            if (!chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, userId)) {
                throw new IllegalStateException("not a member");
            }
        }
        return message;
    }
}
