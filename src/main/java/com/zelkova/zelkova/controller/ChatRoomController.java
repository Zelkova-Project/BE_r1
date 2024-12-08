package com.zelkova.zelkova.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @GetMapping("/rooms")
  public Set<String> getRooms() {
    Set<String> rooms = stringRedisTemplate.keys("chatRoom:*:messages");

    if (rooms == null || rooms.isEmpty()) {
      return Set.of("");
    }

    return rooms.stream()
        .map(key -> key.replace("chatRoom:", "")
            .replace("/topic", "")
            .replace(":messages", "")
            .replace("/", ""))
        .collect(Collectors.toSet());
  }
}
