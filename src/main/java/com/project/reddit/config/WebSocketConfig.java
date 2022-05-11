package com.project.reddit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new PostHandler(), "/ws/post").
                setAllowedOrigins("chrome-extension://cbcbkhdmedgianpaifchdaddpnmgnknn", "http://localhost:8081", "*")
                .addInterceptors()
                .addHandler(new CommentHandler(), "/ws/comment")
                .setAllowedOrigins("chrome-extension://cbcbkhdmedgianpaifchdaddpnmgnknn", "http://localhost:8081", "*");
    }

    /*    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/temp")
                    .setAllowedOrigins("http://localhost:8081/","chrome-extension://cbcbkhdmedgianpaifchdaddpnmgnknn");
                    //.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
registry.enableStompBrokerRelay("/topic");
        registry.setApplicationDestinationPrefixes("/ws");



        registry.setApplicationDestinationPrefixes("/app");
                registry.enableStompBrokerRelay("/topic")
                .setRelayHost("172.18.0.2")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");

    }*/
}
