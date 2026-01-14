package com.example.madcamp_lounge.repository;

import com.example.madcamp_lounge.entity.ChatRoomMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);
    void deleteByRoomIdAndUserId(Long roomId, Long userId);
    long countByRoomId(Long roomId);
    long countByRoomIdAndLastReadMessageIdGreaterThanEqual(Long roomId, Long lastReadMessageId);
    List<ChatRoomMember> findByUserId(Long userId);
    List<ChatRoomMember> findByRoomId(Long roomId);
    List<ChatRoomMember> findByRoomIdIn(List<Long> roomIds);
    ChatRoomMember findByRoomIdAndUserId(Long roomId, Long userId);
}
