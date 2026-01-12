package com.example.madcamp_lounge.service;

import com.example.madcamp_lounge.repository.PartyRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyStatusService {
    private final PartyRepository partyRepository;

    @Transactional
    public void closeExpiredParties() {
        partyRepository.closeExpired(LocalDateTime.now());
    }
}
