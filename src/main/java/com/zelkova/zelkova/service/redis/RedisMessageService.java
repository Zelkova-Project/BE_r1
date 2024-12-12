package com.zelkova.zelkova.service.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Save a message in a Redis List for the chat room
    public void saveMessage(String chatRoomId, String message) {
        String key = "chatRoom:" + chatRoomId + ":messages";  // Redis key for the chat room's message list
        redisTemplate.opsForList().rightPush(key, message);  // Add message to the list (right side)
    }
    
    // Fetch the most recent N messages from the chat room
    public List<String> getRecentMessages(String chatRoomId, int count) {
        String key = "chatRoom:" + chatRoomId + ":messages";
        ListOperations list = redisTemplate.opsForList();
        Long size = list.size(key);

        int start = 0;

        if (size > 10) {
            start = (int)(size - 10L);
        }

        return redisTemplate.opsForList().range(key, start, size); 
    }
}

