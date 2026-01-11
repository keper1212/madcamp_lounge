package com.example.madcamp_lounge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "parties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "host_id", nullable = false)
    private Long hostId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "place_name", nullable = false, length = 100)
    private String placeName;

    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "target_count", nullable = false)
    private Integer targetCount;

    @Column(name = "current_capacity", nullable = false)
    private Integer currentCapacity;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Party(
        Long hostId,
        String title,
        String category,
        String content,
        LocalDateTime appointmentTime,
        String placeName,
        Long placeId,
        Integer targetCount,
        Integer currentCapacity,
        String imageUrl,
        Long chatRoomId,
        String status
    ) {
        this.hostId = hostId;
        this.title = title;
        this.category = category;
        this.content = content;
        this.appointmentTime = appointmentTime;
        this.placeName = placeName;
        this.placeId = placeId;
        this.targetCount = targetCount;
        this.currentCapacity = currentCapacity;
        this.imageUrl = imageUrl;
        this.chatRoomId = chatRoomId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void updateDetails(
        String title,
        String category,
        LocalDateTime appointmentTime,
        String placeName,
        Integer targetCount
    ) {
        if (title != null) {
            this.title = title;
        }
        if (category != null) {
            this.category = category;
        }
        if (appointmentTime != null) {
            this.appointmentTime = appointmentTime;
        }
        if (placeName != null) {
            this.placeName = placeName;
        }
        if (targetCount != null) {
            this.targetCount = targetCount;
        }
    }

    public void incrementCurrentCapacity() {
        if (this.currentCapacity == null) {
            this.currentCapacity = 1;
            return;
        }
        this.currentCapacity += 1;
    }
}
