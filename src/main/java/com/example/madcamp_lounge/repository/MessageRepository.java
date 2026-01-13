package com.example.madcamp_lounge.repository;

import com.example.madcamp_lounge.entity.Message;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop30ByRoomIdOrderBySentAtDesc(Long roomId);
    Message findTop1ByRoomIdOrderBySentAtDesc(Long roomId);
    long countByRoomId(Long roomId);
    long countByRoomIdAndIdGreaterThan(Long roomId, Long messageId);
    List<Message> findTop30ByRoomIdAndSentAtBeforeOrderBySentAtDesc(
        Long roomId,
        LocalDateTime sentAt
    );
    boolean existsByRoomIdAndSentAtBefore(Long roomId, LocalDateTime sentAt);

    @Query("""
        select m from Message m
        where m.roomId = :roomId
          and (m.sentAt < :sentAt or (m.sentAt = :sentAt and m.id < :messageId))
        order by m.sentAt desc, m.id desc
        """)
    List<Message> findTop30ByRoomIdAndCursor(
        @Param("roomId") Long roomId,
        @Param("sentAt") LocalDateTime sentAt,
        @Param("messageId") Long messageId
    );

    @Query("""
        select case when count(m) > 0 then true else false end
        from Message m
        where m.roomId = :roomId
          and (m.sentAt < :sentAt or (m.sentAt = :sentAt and m.id < :messageId))
        """)
    boolean existsByRoomIdAndCursor(
        @Param("roomId") Long roomId,
        @Param("sentAt") LocalDateTime sentAt,
        @Param("messageId") Long messageId
    );
}
