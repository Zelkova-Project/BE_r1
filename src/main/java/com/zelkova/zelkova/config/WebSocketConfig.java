package com.zelkova.zelkova.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // !: STOMP CLIENT BEAN
    // private final WebSocketAuthInterceptor webSocketAuthInterceptor;
    
    // !: STOMP CLIENT BEAN
    // public WebSocketConfig(WebSocketAuthInterceptor webSocketAuthInterceptor) {
    //     this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    // }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Enable a simple in-memory broker
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/chat-websocket")
                .setAllowedOriginPatterns("http://localhost:3000", "https://namu0005.or.kr")
                .withSockJS(); // Define the WebSocket endpoint
    }

    // stompClient로 connect시 제일 먼저 인터셉처 받는 위치. 
    // !: 미사용을 원한다면 stompClient관련 bean생성만 막으면 됨
    // !: 설정은 'WebSocketAuthInterceptor' 
    // @Override
    // public void configureClientInboundChannel(ChannelRegistration registration) {
    //     registration.interceptors(webSocketAuthInterceptor);
    // }
}

