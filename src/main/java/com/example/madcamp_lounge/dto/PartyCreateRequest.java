package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyCreateRequest {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "category is required")
    private String category;

    @NotBlank(message = "time is required")
    @JsonProperty("appointment_time")
    private String time;

    @NotBlank(message = "place_name is required")
    @JsonProperty("place_name")
    private String placeName;

    @NotNull(message = "target_count is required")
    @Min(value = 1, message = "target_count must be at least 1")
    @JsonProperty("target_count")
    private Integer targetCount;
}
