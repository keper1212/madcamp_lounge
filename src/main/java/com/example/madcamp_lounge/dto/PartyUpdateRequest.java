package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyUpdateRequest {
    @NotNull(message = "party_id is required")
    @JsonProperty("party_id")
    private Long partyId;

    private String title;
    private String category;
    private String content;
    @JsonProperty("appointment_time")
    private String time;

    @JsonProperty("place_name")
    private String placeName;

    @Min(value = 1, message = "target_count must be at least 1")
    @JsonProperty("target_count")
    private Integer targetCount;
}
