package com.project.reddit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class CommentHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();



    @Override

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.add(session);

        super.afterConnectionEstablished(session);

    }



    @Override

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {



        sessions.remove(session);

        super.afterConnectionClosed(session, status);

    }



    @Override

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        super.handleTextMessage(session, message);

        sessions.forEach(webSocketSession -> {

            try {
                log.info("Comment handler");
                webSocketSession.sendMessage(message);

            } catch (IOException e) {

                log.error(e.getMessage());

            }

        });

    }

}
