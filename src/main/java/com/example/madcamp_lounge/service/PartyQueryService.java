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
    public List<PartyDetailResponse> listParties() {
        List<Party> parties = partyRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Party party : parties) {
            if (party.getAppointmentTime() != null
                && party.getAppointmentTime().isBefore(now)
                && !"CLOSED".equals(party.getStatus())) {
                party.close();
            }
        }
        List<Long> partyIds = parties.stream()
            .map(Party::getId)
            .toList();
        List<PartyMember> allMembers = partyMemberRepository.findByPartyIdIn(partyIds);
        Map<Long, List<PartyMember>> membersByParty = allMembers.stream()
            .collect(Collectors.groupingBy(PartyMember::getPartyId));

        List<Long> userIds = allMembers.stream()
            .map(PartyMember::getUserId)
            .distinct()
            .toList();
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, user -> user));

        return parties.stream()
            .map(party -> {
                List<PartyMember> members = membersByParty.getOrDefault(party.getId(), List.of());
                List<PartyMemberResponse> memberResponses = members.stream()
                    .map(member -> userMap.get(member.getUserId()))
                    .filter(user -> user != null)
                    .map(PartyMemberResponse::from)
                    .toList();
                return PartyDetailResponse.from(party, memberResponses);
            })
            .toList();
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
            .distinct()
            .toList();
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, user -> user));
        List<PartyMemberResponse> memberResponses = members.stream()
            .map(member -> userMap.get(member.getUserId()))
            .filter(user -> user != null)
            .map(PartyMemberResponse::from)
            .toList();

        return Optional.of(PartyDetailResponse.from(party.get(), memberResponses));
    }
}
