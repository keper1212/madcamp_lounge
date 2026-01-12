package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.Party;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyDetailResponse {
    private Long id;
    @JsonProperty("host_id")
    private Long hostId;
    private String title;
    private String category;
    private String content;
    @JsonProperty("appointment_time")
    private LocalDateTime appointmentTime;
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("target_count")
    private Integer targetCount;
    @JsonProperty("current_capacity")
    private Integer currentCapacity;
    private String status;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private List<PartyMemberResponse> members;

    public static PartyDetailResponse from(Party party, List<PartyMemberResponse> members) {
        return new PartyDetailResponse(
            party.getId(),
            party.getHostId(),
            party.getTitle(),
            party.getCategory(),
            party.getContent(),
            party.getAppointmentTime(),
            party.getPlaceName(),
            party.getTargetCount(),
            party.getCurrentCapacity(),
            party.getStatus(),
            party.getCreatedAt(),
            members
        );
    }
}
