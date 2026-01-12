package com.example.madcamp_lounge.repository;

import com.example.madcamp_lounge.entity.Message;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop30ByRoomIdOrderBySentAtDesc(Long roomId);
    boolean existsByRoomIdAndSentAtBefore(Long roomId, LocalDateTime sentAt);
}
