package com.zelkova.zelkova.service.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatSubscriber {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisMessageService redisMessageService;

    // Send the received message to WebSocket clients
    public void onMessage(String chatRoomId, String message) {
        // Send the new message to WebSocket clients subscribed to the chat room
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, message);
    }

    // Fetch and send the last N messages to a user who joins a chat room
    public void sendChatHistory(String chatRoomId, int count) {
        List<String> recentMessages = redisMessageService.getRecentMessages(chatRoomId, count);
        // Send the chat history to the new user (via WebSocket)
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, recentMessages);
    }
}