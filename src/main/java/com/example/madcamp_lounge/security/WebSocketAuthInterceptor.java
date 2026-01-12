package com.example.madcamp_lounge.security;

import io.jsonwebtoken.Claims;
import java.util.Collections;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public WebSocketAuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String header = accessor.getFirstNativeHeader("Authorization");
            if (header == null) {
                header = accessor.getFirstNativeHeader("authorization");
            }
            if (header == null || !header.startsWith("Bearer ")) {
                throw new IllegalArgumentException("missing auth token");
            }

            String token = header.substring("Bearer ".length());
            Claims claims = jwtTokenProvider.parseClaims(token);
            String type = claims.get("type", String.class);
            if (!"access".equals(type)) {
                throw new IllegalArgumentException("invalid token type");
            }

            String userId = claims.getSubject();
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
            accessor.setUser(authentication);
        }
        return message;
    }
}
