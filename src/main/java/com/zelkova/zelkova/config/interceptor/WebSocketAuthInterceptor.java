package com.zelkova.zelkova.config.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.messaging.support.ChannelInterceptor;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Extract the Authorization header
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7); // Extract the token
            // Validate the token (use your token validation logic here)
            boolean isValid = validateToken(token);
            if (!isValid) {
                throw new IllegalArgumentException("Invalid token");
            }
        }

        return message;
    }

    private boolean validateToken(String token) {
        // Replace with your token validation logic
        return token.equals("your-access-token-here");
    }
}


