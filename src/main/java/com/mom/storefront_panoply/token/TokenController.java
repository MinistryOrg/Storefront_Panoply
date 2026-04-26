package com.mom.storefront_panoply.token;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TwitchAuthService twitchAuthService;

    @PostMapping("/refresh-token")
    public Map<String, Object> refreshToken() {
        String token = twitchAuthService.getValidToken(); // forces refresh if expired

        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Token refreshed successfully");
        response.put("token_preview", token.substring(0, Math.min(10, token.length())) + "...");

        return response;
    }
}
