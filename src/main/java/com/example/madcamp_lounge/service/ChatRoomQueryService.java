package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.dto.ChatHistoryResponse;
import com.example.madcamp_lounge.dto.ChatMessageResponse;
import com.example.madcamp_lounge.dto.ChatRoomDetailResponse;
import com.example.madcamp_lounge.dto.ChatRoomListResponse;
import com.example.madcamp_lounge.dto.PartyMemberResponse;
import com.example.madcamp_lounge.entity.ChatRoom;
import com.example.madcamp_lounge.entity.ChatRoomMember;
import com.example.madcamp_lounge.entity.Message;
import com.example.madcamp_lounge.entity.Party;
import com.example.madcamp_lounge.entity.User;
import com.example.madcamp_lounge.repository.ChatRoomMemberRepository;
import com.example.madcamp_lounge.repository.ChatRoomRepository;
import com.example.madcamp_lounge.repository.MessageRepository;
import com.example.madcamp_lounge.repository.PartyRepository;
import com.example.madcamp_lounge.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomListResponse> listRooms(Long userId) {
        List<ChatRoomMember> memberships = chatRoomMemberRepository.findByUserId(userId);
        List<Long> roomIds = memberships.stream()
            .map(ChatRoomMember::getRoomId)
            .distinct()
            .toList();
        if (roomIds.isEmpty()) {
            return List.of();
        }
        Map<Long, ChatRoomMember> membershipMap = memberships.stream()
            .collect(Collectors.toMap(
                ChatRoomMember::getRoomId,
                member -> member,
                (first, second) -> first
            ));

        List<ChatRoom> rooms = chatRoomRepository.findAllById(roomIds);
        return rooms.stream()
            .map(room -> {
                ChatRoomMember member = membershipMap.get(room.getId());
                long unreadCount = 0;
                if (member != null) {
                    Long lastReadMessageId = member.getLastReadMessageId();
                    if (lastReadMessageId == null) {
                        unreadCount = messageRepository.countByRoomId(room.getId());
                    } else {
                        unreadCount = messageRepository.countByRoomIdAndIdGreaterThan(
                            room.getId(),
                            lastReadMessageId
                        );
                    }
                }
                return ChatRoomListResponse.from(room, unreadCount);
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ChatRoomDetailResponse> getChatRoomDetail(Long userId, Long roomId) {
        Optional<ChatRoom> room = chatRoomRepository.findById(roomId);
        if (room.isEmpty()) {
            return Optional.empty();
        }
        if (!chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new IllegalStateException("not a member");
        }

        List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(roomId);
        List<Long> userIds = members.stream()
            .map(ChatRoomMember::getUserId)
            .distinct()
            .toList();
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, user -> user));
        List<PartyMemberResponse> memberResponses = members.stream()
            .map(member -> userMap.get(member.getUserId()))
            .filter(user -> user != null)
            .map(PartyMemberResponse::from)
            .toList();

        List<Message> messages = messageRepository.findTop30ByRoomIdOrderBySentAtDesc(roomId);
        List<ChatMessageResponse> messageResponses = messages.stream()
            .map(ChatMessageResponse::from)
            .toList();
        ChatMessageResponse lastMessage = messageResponses.isEmpty()
            ? null
            : messageResponses.get(messageResponses.size() - 1);

        boolean hasMore = false;
        if (lastMessage != null) {
            hasMore = messageRepository.existsByRoomIdAndCursor(
                roomId,
                lastMessage.getSentAt(),
                lastMessage.getMessageId()
            );
        }

        String partyTitle = null;
        if (room.get().getPartyId() != null) {
            Optional<Party> party = partyRepository.findById(room.get().getPartyId());
            if (party.isPresent()) {
                partyTitle = party.get().getTitle();
            }
        }

        ChatRoomDetailResponse.ChatRoomInfo info = new ChatRoomDetailResponse.ChatRoomInfo(
            room.get().getId(),
            room.get().getPartyId(),
            partyTitle,
            room.get().getCreatedAt()
        );

        return Optional.of(new ChatRoomDetailResponse(
            info,
            memberResponses,
            messageResponses,
            lastMessage == null ? null : lastMessage.getSentAt(),
            lastMessage == null ? null : lastMessage.getMessageId(),
            hasMore
        ));
    }

    @Transactional(readOnly = true)
    public Optional<ChatHistoryResponse> getChatHistory(
        Long userId,
        Long roomId,
        LocalDateTime nextCursor,
        Long nextCursorId
    ) {
        if (!chatRoomRepository.existsById(roomId)) {
            return Optional.empty();
        }
        if (!chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new IllegalStateException("not a member");
        }

        List<Message> messages;
        if (nextCursor == null) {
            messages = messageRepository.findTop30ByRoomIdOrderBySentAtDesc(roomId);
        } else if (nextCursorId != null) {
            messages = messageRepository.findTop30ByRoomIdAndCursor(
                roomId,
                nextCursor,
                nextCursorId
            );
        } else {
            messages = messageRepository.findTop30ByRoomIdAndSentAtBeforeOrderBySentAtDesc(
                roomId,
                nextCursor
            );
        }

        List<ChatMessageResponse> messageResponses = messages.stream()
            .map(ChatMessageResponse::from)
            .toList();
        ChatMessageResponse lastMessage = messageResponses.isEmpty()
            ? null
            : messageResponses.get(messageResponses.size() - 1);

        boolean hasMore = false;
        if (lastMessage != null) {
            hasMore = messageRepository.existsByRoomIdAndCursor(
                roomId,
                lastMessage.getSentAt(),
                lastMessage.getMessageId()
            );
        }

        return Optional.of(new ChatHistoryResponse(
            messageResponses,
            lastMessage == null ? null : lastMessage.getSentAt(),
            lastMessage == null ? null : lastMessage.getMessageId(),
            hasMore
        ));
    }
}
