package com.zelkova.zelkova.service;

import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber {
 public void receiveMessage(String message) {
  System.out.println("Received message from Redis: " + message);
  // Logic to send the message over WebSocket
 }
}
