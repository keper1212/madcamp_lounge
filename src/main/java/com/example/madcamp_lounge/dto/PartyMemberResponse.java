package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyMemberResponse {
    @JsonProperty("user_id")
    private Long userId;
    private String nickname;
    private String name;
    @JsonProperty("class_section")
    private String classSection;

    public static PartyMemberResponse from(User user) {
        return new PartyMemberResponse(
            user.getId(),
            user.getNickname(),
            user.getName(),
            user.getClassSection()
        );
    }
}
