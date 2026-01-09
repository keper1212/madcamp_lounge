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

    // /* ---------- 생성 / 변경 로직 ---------- */

    // @PrePersist
    // protected void onCreate() {
    // this.createdAt = LocalDateTime.now();
    // }

    // public static User create(
    // String loginId,
    // String password,
    // String name) {
    // User user = new User();
    // user.loginId = loginId;
    // user.password = password;
    // user.name = name;
    // user.isFirstLogin = true;
    // return user;
    // }

    // public void changePassword(String newPassword) {
    // this.password = newPassword;
    // this.isFirstLogin = false;
    // }

    // public void updateProfile(
    // String nickname,
    // String mbti,
    // String classSection,
    // String hobby,
    // String introduction) {
    // this.nickname = nickname;
    // this.mbti = mbti;
    // this.classSection = classSection;
    // this.hobby = hobby;
    // this.introduction = introduction;
    // }
}