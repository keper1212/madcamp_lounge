package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.dto.ChatRoomListResponse;
import com.example.madcamp_lounge.entity.ChatRoom;
import com.example.madcamp_lounge.entity.ChatRoomMember;
import com.example.madcamp_lounge.repository.ChatRoomMemberRepository;
import com.example.madcamp_lounge.repository.ChatRoomRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

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
        List<ChatRoom> rooms = chatRoomRepository.findAllById(roomIds);
        return rooms.stream()
            .map(ChatRoomListResponse::from)
            .collect(Collectors.toList());
    }
}
