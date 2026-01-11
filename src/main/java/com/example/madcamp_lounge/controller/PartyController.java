package com.example.madcamp_lounge.controller;

import com.example.madcamp_lounge.dto.PartyCreateRequest;
import com.example.madcamp_lounge.dto.PartyResponse;
import com.example.madcamp_lounge.dto.PartyUpdateRequest;
import com.example.madcamp_lounge.entity.Party;
import com.example.madcamp_lounge.service.PartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {
    private final PartyService partyService;

    @PostMapping("/create")
    public ResponseEntity<PartyResponse> create(@Valid @RequestBody PartyCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId;
        try {
            userId = Long.parseLong(auth.getPrincipal().toString());
        } catch (NumberFormatException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Party party = partyService.createParty(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PartyResponse.from(party));
    }

    @PatchMapping("/edit")
    public ResponseEntity<PartyResponse> edit(@Valid @RequestBody PartyUpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId;
        try {
            userId = Long.parseLong(auth.getPrincipal().toString());
        } catch (NumberFormatException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Party> party = partyService.updateParty(userId, request);
        return party
            .map(value -> ResponseEntity.ok(PartyResponse.from(value)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleBadRequest() {
        return ResponseEntity.badRequest().build();
    }
}
