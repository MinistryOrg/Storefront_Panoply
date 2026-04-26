package com.mom.storefront_panoply.igdb.clients.config;

import com.mom.storefront_panoply.token.TwitchAuthService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignIgdbConfig {

    private final TwitchAuthService twitchAuthService;

    @Value("${igdb.client-id}")
    private String clientId;

    @Bean
    public RequestInterceptor igdbAuthInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {

                template.header("Client-ID", clientId);

                // 🔥 ALWAYS fresh token
                String token = twitchAuthService.getValidToken();
                template.header("Authorization", "Bearer " + token);

                template.header("Content-Type", "text/plain");
                template.header("Accept", "application/json");
            }
        };
    }
}