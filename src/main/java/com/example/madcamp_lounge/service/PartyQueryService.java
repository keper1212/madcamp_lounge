package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.entity.Party;
import com.example.madcamp_lounge.repository.PartyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyQueryService {
    private final PartyRepository partyRepository;

    @Transactional(readOnly = true)
    public List<Party> listParties() {
        return partyRepository.findAll();
    }
}
