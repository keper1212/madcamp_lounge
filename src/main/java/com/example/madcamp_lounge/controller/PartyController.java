package com.example.madcamp_lounge.controller;

import com.example.madcamp_lounge.dto.PartyCreateRequest;
import com.example.madcamp_lounge.dto.PartyDetailResponse;
import com.example.madcamp_lounge.dto.PartyJoinRequest;
import com.example.madcamp_lounge.dto.PartyResponse;
import com.example.madcamp_lounge.dto.PartyUpdateRequest;
import com.example.madcamp_lounge.entity.Party;
import com.example.madcamp_lounge.service.PartyQueryService;
import com.example.madcamp_lounge.service.PartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {
    private final PartyService partyService;
    private final PartyQueryService partyQueryService;

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

    @PostMapping("/join")
    public ResponseEntity<PartyResponse> join(@Valid @RequestBody PartyJoinRequest request) {
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

        Optional<Party> party = partyService.joinParty(userId, request);
        return party
            .map(value -> ResponseEntity.ok(PartyResponse.from(value)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/exit")
    public ResponseEntity<PartyResponse> exit(@Valid @RequestBody PartyJoinRequest request) {
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

        Optional<Party> party = partyService.exitParty(userId, request);
        return party
            .map(value -> ResponseEntity.ok(PartyResponse.from(value)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/list")
    public ResponseEntity<List<PartyDetailResponse>> list() {
        List<PartyDetailResponse> responses = partyQueryService.listParties();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/detail")
    public ResponseEntity<PartyDetailResponse> detail(@RequestParam("party_id") Long partyId) {
        return partyQueryService.getPartyDetail(partyId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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

    @PatchMapping("/delete")
    public ResponseEntity<PartyResponse> delete(@Valid @RequestBody PartyJoinRequest request) {
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

        Optional<Party> party = partyService.closeParty(userId, request);
        return party
            .map(value -> ResponseEntity.ok(PartyResponse.from(value)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleBadRequest() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Void> handleConflict() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
