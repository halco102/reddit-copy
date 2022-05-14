/*
package com.project.reddit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
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



}
*/
