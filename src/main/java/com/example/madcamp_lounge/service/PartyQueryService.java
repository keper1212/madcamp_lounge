package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.dto.PartyDetailResponse;
import com.example.madcamp_lounge.dto.PartyMemberResponse;
import com.example.madcamp_lounge.entity.Party;
import com.example.madcamp_lounge.entity.PartyMember;
import com.example.madcamp_lounge.entity.User;
import com.example.madcamp_lounge.repository.PartyMemberRepository;
import com.example.madcamp_lounge.repository.PartyRepository;
import com.example.madcamp_lounge.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyQueryService {
    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Party> listParties() {
        List<Party> parties = partyRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Party party : parties) {
            if (party.getAppointmentTime() != null
                && party.getAppointmentTime().isBefore(now)
                && !"CLOSED".equals(party.getStatus())) {
                party.close();
            }
        }
        return parties;
    }

    @Transactional(readOnly = true)
    public Optional<PartyDetailResponse> getPartyDetail(Long partyId) {
        Optional<Party> party = partyRepository.findById(partyId);
        if (party.isEmpty()) {
            return Optional.empty();
        }

        List<PartyMember> members = partyMemberRepository.findByPartyId(partyId);
        List<Long> userIds = members.stream()
            .map(PartyMember::getUserId)
            .toList();
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, user -> user));
        List<PartyMemberResponse> memberResponses = userIds.stream()
            .map(userMap::get)
            .filter(user -> user != null)
            .map(PartyMemberResponse::from)
            .toList();

        return Optional.of(PartyDetailResponse.from(party.get(), memberResponses));
    }
}
