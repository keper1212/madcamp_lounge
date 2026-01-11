package com.example.madcamp_lounge.dto;

import com.example.madcamp_lounge.entity.Party;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyResponse {
    private Long id;
    @JsonProperty("host_id")
    private Long hostId;
    private String title;
    private String category;
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

    public static PartyResponse from(Party party) {
        return new PartyResponse(
            party.getId(),
            party.getHostId(),
            party.getTitle(),
            party.getCategory(),
            party.getAppointmentTime(),
            party.getPlaceName(),
            party.getTargetCount(),
            party.getCurrentCapacity(),
            party.getStatus(),
            party.getCreatedAt()
        );
    }
}
