package com.mom.storefront_panoply.token;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TwitchConfig {
    private final TwitchAuthService twitchAuthService;

    @Value("${igdb.client-id}")
    private String clientId;

    @Bean
    public RequestInterceptor igdbRequestInterceptor() {
        return template -> {
            template.header("Client-ID", clientId);
            template.header("Authorization", "Bearer " + twitchAuthService.getValidToken());
        };
    }
}
