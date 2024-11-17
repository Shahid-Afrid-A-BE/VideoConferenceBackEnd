package com.tehyt.videoConferenceToolBE.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessionsList = new HashSet<>();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON parser

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected: " + session.getId());
       // session.sendMessage(new TextMessage("{\"type\": \"welcome\", \"message\": \"Connected to WebSocket server\"}"));
        sessionsList.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());

        // Parse incoming message as JSON
        try {
            // Check if the message is valid JSON...
            var jsonNode = objectMapper.readTree(message.getPayload());
            if (jsonNode.has("type")) {
                System.out.println("Signaling message type: " + jsonNode.get("type").asText());

                // Relay the signaling message to all other clients except the sender
                for (WebSocketSession s : sessionsList) {
                    if (s != session && s.isOpen()) {
                        s.sendMessage(message);
                    }
                }
            } else {
                System.err.println("Invalid message received, missing 'type' field: " + message.getPayload());
            }
        } catch (Exception e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Disconnected: " + session.getId());
        sessionsList.remove(session);
    }
}
