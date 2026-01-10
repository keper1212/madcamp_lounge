package com.example.madcamp_lounge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String nickname;

    @Column(length = 10)
    private String mbti;

    @Column(name = "class_section", length = 50)
    private String classSection;

    @Column(columnDefinition = "TEXT")
    private String hobby;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "is_first_login", nullable = false)
    private Boolean isFirstLogin = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.isFirstLogin = false;
    }

    public void updateProfile(
        String nickname,
        String hobby,
        String introduction
    ) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (hobby != null) {
            this.hobby = hobby;
        }
        if (introduction != null) {
            this.introduction = introduction;
        }
    }
}
