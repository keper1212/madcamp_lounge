package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.entity.Message;
import com.example.madcamp_lounge.entity.ChatRoomMember;
import com.example.madcamp_lounge.repository.ChatRoomMemberRepository;
import com.example.madcamp_lounge.repository.MessageRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public Message saveMessage(Long roomId, Long userId, String content) {
        if (!chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new IllegalStateException("not a member");
        }
        Message message = new Message(roomId, userId, content);
        return messageRepository.save(message);
    }

    @Transactional
    public LocalDateTime markRead(Long roomId, Long userId, Long lastMessageId) {
        ChatRoomMember member = chatRoomMemberRepository.findByRoomIdAndUserId(roomId, userId);
        if (member == null) {
            throw new IllegalStateException("not a member");
        }
        member.markRead(lastMessageId);
        return member.getLastReadAt();
    }
}
