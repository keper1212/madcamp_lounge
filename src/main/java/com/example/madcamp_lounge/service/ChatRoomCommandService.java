package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.entity.ChatRoom;
import com.example.madcamp_lounge.entity.ChatRoomMember;
import com.example.madcamp_lounge.repository.ChatRoomMemberRepository;
import com.example.madcamp_lounge.repository.ChatRoomRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomCommandService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public ChatRoom findOrCreateDirectRoom(Long userId, Long otherUserId) {
        if (userId.equals(otherUserId)) {
            throw new IllegalArgumentException("cannot create chat with self");
        }

        List<ChatRoomMember> userMemberships = chatRoomMemberRepository.findByUserId(userId);
        List<ChatRoomMember> otherMemberships = chatRoomMemberRepository.findByUserId(otherUserId);
        Set<Long> userRoomIds = new HashSet<>();
        for (ChatRoomMember member : userMemberships) {
            userRoomIds.add(member.getRoomId());
        }
        Set<Long> otherRoomIds = new HashSet<>();
        for (ChatRoomMember member : otherMemberships) {
            otherRoomIds.add(member.getRoomId());
        }

        userRoomIds.retainAll(otherRoomIds);
        for (Long roomId : userRoomIds) {
            Optional<ChatRoom> room = chatRoomRepository.findById(roomId);
            if (room.isEmpty() || room.get().getPartyId() != null) {
                continue;
            }
            List<ChatRoomMember> members = chatRoomMemberRepository.findByRoomId(roomId);
            if (members.size() == 2 &&
                members.stream().anyMatch(member -> member.getUserId().equals(userId)) &&
                members.stream().anyMatch(member -> member.getUserId().equals(otherUserId))) {
                return room.get();
            }
        }

        ChatRoom newRoom = chatRoomRepository.save(new ChatRoom(null));
        chatRoomMemberRepository.save(new ChatRoomMember(newRoom.getId(), userId));
        chatRoomMemberRepository.save(new ChatRoomMember(newRoom.getId(), otherUserId));
        return newRoom;
    }
}
