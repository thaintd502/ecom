package com.ecom2.config;

import com.ecom2.handler.CustomHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final CustomHandshakeHandler customHandshakeHandler;

    public WebSocketConfig(CustomHandshakeHandler customHandshakeHandler) {
        this.customHandshakeHandler = customHandshakeHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Cho phép client SUBSCRIBE vào các topic
        config.enableSimpleBroker("/topic", "/queue");  // queue dùng cho user riêng
        // Prefix client sẽ dùng để gửi message
        config.setApplicationDestinationPrefixes("/app");
        // Gửi message đến user sử dụng convertAndSendToUser
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // endpoint WebSocket
                .setHandshakeHandler(customHandshakeHandler)
                .setAllowedOriginPatterns("http://localhost:3000/")
                .withSockJS(); // dùng SockJS fallback nếu trình duyệt không hỗ trợ WebSocket
    }
}
