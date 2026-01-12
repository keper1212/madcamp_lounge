package com.example.madcamp_lounge.repository;

import com.example.madcamp_lounge.entity.PartyMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    boolean existsByPartyIdAndUserId(Long partyId, Long userId);
    List<PartyMember> findByPartyId(Long partyId);
    void deleteByPartyIdAndUserId(Long partyId, Long userId);
}
