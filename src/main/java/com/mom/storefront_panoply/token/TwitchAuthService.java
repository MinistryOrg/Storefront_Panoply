package com.mom.storefront_panoply.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchAuthService {

    @Value("${igdb.client-id}")
    private String clientId;

    @Value("${igdb.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private String token;
    private long expiryTime;

    public synchronized String getValidToken() {
        if (token == null || System.currentTimeMillis() >= expiryTime) {
            log.info("Token expired or missing. Refreshing...");
            refreshToken();
        }
        return token;
    }

    private void refreshToken() {
        String url = "https://id.twitch.tv/oauth2/token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";

        TwitchTokenResponse response =
                restTemplate.postForObject(url, null, TwitchTokenResponse.class);

        if (response == null || response.getAccessToken() == null) {
            throw new RuntimeException("Failed to retrieve Twitch token");
        }

        this.token = response.getAccessToken();

        long expiresInSeconds = response.getExpiresIn();

        // ✅ refresh 1 day before expiration
        long refreshBeforeSeconds = 86400;

        long effectiveExpiry = Math.max(0, expiresInSeconds - refreshBeforeSeconds);

        this.expiryTime = System.currentTimeMillis() + (effectiveExpiry * 1000L);

        log.info("New token fetched. expires_in={}s, refresh_in={}s",
                expiresInSeconds, effectiveExpiry);
    }
}
