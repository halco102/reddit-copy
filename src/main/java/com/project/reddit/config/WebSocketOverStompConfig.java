package com.project.reddit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketOverStompConfig implements WebSocketMessageBrokerConfigurer {

        @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

            registry.addEndpoint("/ws")
                    .setAllowedOrigins("http://localhost:8081/", "http://localhost:8080/", "https://demo-reddit-admir-halilovic.herokuapp.com", "chrome-extension://cbcbkhdmedgianpaifchdaddpnmgnknn", "http://172.18.0.5:8080");
                    //.withSockJS();
    }



    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");

    }
}
