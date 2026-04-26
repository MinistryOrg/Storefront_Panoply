package com.mom.storefront_panoply.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final TwitchAuthService twitchAuthService;

    // runs every 6 hours
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 6)
    public void refreshTokenPeriodically() {
        log.info("Scheduled token refresh check...");
        twitchAuthService.getValidToken();
    }
}
