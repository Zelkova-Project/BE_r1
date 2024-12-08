package com.zelkova.zelkova.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelkova.zelkova.dto.ChatDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class ChatPublisher {

    @Autowired
    private RedisMessageService redisMessageService;

    private final SimpMessagingTemplate template;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void sendMessage(String chatRoomId, ChatDTO chatDTO) {
        log.debug("Debug >>>> sendMessage " + chatDTO.toString());

        try {
            String jsonMessage = objectMapper.writeValueAsString(chatDTO);
            redisMessageService.saveMessage(chatRoomId, jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        template.convertAndSend(chatRoomId, chatDTO);
    }
}
