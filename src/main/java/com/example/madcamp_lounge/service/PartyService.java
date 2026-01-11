package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.dto.PartyCreateRequest;
import com.example.madcamp_lounge.dto.PartyUpdateRequest;
import com.example.madcamp_lounge.entity.Party;
import com.example.madcamp_lounge.repository.PartyRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;

    @Transactional
    public Party createParty(Long hostId, PartyCreateRequest request) {
        LocalDateTime appointmentTime;
        try {
            appointmentTime = LocalDateTime.parse(request.getTime());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("time must be ISO-8601 format", ex);
        }

        Party party = new Party(
            hostId,
            request.getTitle(),
            request.getCategory(),
            null,
            appointmentTime,
            request.getPlaceName(),
            null,
            request.getTargetCount(),
            1,
            null,
            null,
            "OPEN"
        );
        return partyRepository.save(party);
    }

    @Transactional
    public Optional<Party> updateParty(Long hostId, PartyUpdateRequest request) {
        Optional<Party> optionalParty = partyRepository.findById(request.getPartyId());
        if (optionalParty.isEmpty()) {
            return Optional.empty();
        }

        Party party = optionalParty.get();
        if (!party.getHostId().equals(hostId)) {
            return Optional.empty();
        }

        LocalDateTime appointmentTime = null;
        if (StringUtils.hasText(request.getTime())) {
            try {
                appointmentTime = LocalDateTime.parse(request.getTime());
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("time must be ISO-8601 format", ex);
            }
        }

        String title = StringUtils.hasText(request.getTitle()) ? request.getTitle() : null;
        String category = StringUtils.hasText(request.getCategory()) ? request.getCategory() : null;
        String placeName = StringUtils.hasText(request.getPlaceName()) ? request.getPlaceName() : null;
        Integer targetCount = request.getTargetCount();

        party.updateDetails(title, category, appointmentTime, placeName, targetCount);
        return Optional.of(party);
    }
}
