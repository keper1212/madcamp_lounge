package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.dto.PartyCreateRequest;
import com.example.madcamp_lounge.dto.PartyJoinRequest;
import com.example.madcamp_lounge.dto.PartyUpdateRequest;
import com.example.madcamp_lounge.entity.Party;
import com.example.madcamp_lounge.entity.PartyMember;
import com.example.madcamp_lounge.repository.PartyMemberRepository;
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
    private final PartyMemberRepository partyMemberRepository;

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
            request.getContent(),
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
        String content = StringUtils.hasText(request.getContent()) ? request.getContent() : null;
        String placeName = StringUtils.hasText(request.getPlaceName()) ? request.getPlaceName() : null;
        Integer targetCount = request.getTargetCount();

        party.updateDetails(title, category, appointmentTime, placeName, targetCount);
        if (content != null) {
            party.updateContent(content);
        }
        return Optional.of(party);
    }

    @Transactional
    public Optional<Party> joinParty(Long userId, PartyJoinRequest request) {
        Optional<Party> optionalParty = partyRepository.findById(request.getPartyId());
        if (optionalParty.isEmpty()) {
            return Optional.empty();
        }

        Party party = optionalParty.get();
        if (!"OPEN".equals(party.getStatus())) {
            throw new IllegalStateException("party is not open");
        }
        if (partyMemberRepository.existsByPartyIdAndUserId(party.getId(), userId)) {
            throw new IllegalStateException("already joined");
        }
        if (party.getCurrentCapacity() >= party.getTargetCount()) {
            throw new IllegalStateException("party is full");
        }

        partyMemberRepository.save(new PartyMember(party.getId(), userId));
        party.incrementCurrentCapacity();
        party.updateStatusIfFull();
        return Optional.of(party);
    }

    @Transactional
    public Optional<Party> exitParty(Long userId, PartyJoinRequest request) {
        Optional<Party> optionalParty = partyRepository.findById(request.getPartyId());
        if (optionalParty.isEmpty()) {
            return Optional.empty();
        }

        Party party = optionalParty.get();
        if (party.getHostId().equals(userId)) {
            throw new IllegalStateException("host cannot exit");
        }
        if (!partyMemberRepository.existsByPartyIdAndUserId(party.getId(), userId)) {
            return Optional.empty();
        }

        partyMemberRepository.deleteByPartyIdAndUserId(party.getId(), userId);
        party.decrementCurrentCapacity();
        if ("FULL".equals(party.getStatus())) {
            party.updateStatusIfOpen();
        }
        return Optional.of(party);
    }

    @Transactional
    public Optional<Party> closeParty(Long hostId, PartyJoinRequest request) {
        Optional<Party> optionalParty = partyRepository.findById(request.getPartyId());
        if (optionalParty.isEmpty()) {
            return Optional.empty();
        }

        Party party = optionalParty.get();
        if (!party.getHostId().equals(hostId)) {
            return Optional.empty();
        }

        party.close();
        return Optional.of(party);
    }
}
