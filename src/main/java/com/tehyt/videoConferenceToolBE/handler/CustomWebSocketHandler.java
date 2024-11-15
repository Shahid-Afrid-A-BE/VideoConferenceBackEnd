package com.tehyt.videoConferenceToolBE.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Component      //can be used as bean in other class as dependency injection
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private Set<WebSocketSession> sessionsList = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session)throws Exception
    {
        System.out.println("afr: connected "+session.getId());
        session.sendMessage(new TextMessage("{\"message\": \"Welcome connected with afr websocket server\"}"));
        sessionsList.add(session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        // Echo message back to the client
        // Relay the incoming message to all other clients except the sender
        for (WebSocketSession s : sessionsList) {
            if (s != session && s.isOpen()) {
                s.sendMessage(message);  // Forward the signaling message to other clients
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Disconnected: " + session.getId());
        sessionsList.remove(session);
    }
}
