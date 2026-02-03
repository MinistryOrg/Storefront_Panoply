package com.mom.storefront_panoply.clients.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignIgdbConfig {

    @Value("${igdb.client-id}")
    private String clientId;

    @Value("${igdb.bearer-token}")
    private String bearerToken;

    @Bean
    public RequestInterceptor igdbAuthInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("Client-ID", clientId);
                template.header("Authorization", "Bearer " + bearerToken);
                // ensure IGDB expects text/plain bodies
                template.header("Content-Type", "text/plain");
                template.header("Accept", "application/json");
            }
        };
    }
}