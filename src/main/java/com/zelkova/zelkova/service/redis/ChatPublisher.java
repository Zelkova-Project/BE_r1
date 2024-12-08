package com.zelkova.zelkova.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.zelkova.zelkova.dto.ChatDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatPublisher {

    @Autowired
    private RedisMessageService redisMessageService;

    private final SimpMessagingTemplate template;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void sendMessage(String chatRoomId, ChatDTO chatDTO) {
        // Save the message to Redis
        redisMessageService.saveMessage(chatRoomId, chatDTO.toString());

        // Publish the message to the Redis Pub/Sub channel
        // redisTemplate.convertAndSend(chatRoomId, message); // Publish to the chat room channel
        
        template.convertAndSend(chatRoomId, chatDTO);
    }
}
