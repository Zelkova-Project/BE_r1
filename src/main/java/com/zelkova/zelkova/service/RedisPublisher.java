package com.zelkova.zelkova.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

 private final RedisTemplate<String, Object> redisTemplate;

 public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
  this.redisTemplate = redisTemplate;
 }

 public void publish(String message) {
  redisTemplate.convertAndSend("chat", message);
 }
}
