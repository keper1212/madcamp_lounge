package com.example.madcamp_lounge.repository;

import com.example.madcamp_lounge.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
