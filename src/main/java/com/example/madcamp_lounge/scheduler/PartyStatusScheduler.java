package com.example.madcamp_lounge.scheduler;

import com.example.madcamp_lounge.service.PartyStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartyStatusScheduler {
    private final PartyStatusService partyStatusService;

    @Scheduled(fixedDelay = 60000)
    public void closeExpiredParties() {
        partyStatusService.closeExpiredParties();
    }
}
