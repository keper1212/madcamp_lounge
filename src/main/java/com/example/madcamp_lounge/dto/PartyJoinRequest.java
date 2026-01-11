package com.example.madcamp_lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyJoinRequest {
    @NotNull(message = "party_id is required")
    @JsonProperty("party_id")
    private Long partyId;
}
