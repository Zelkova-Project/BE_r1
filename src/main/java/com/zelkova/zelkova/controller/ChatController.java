package com.zelkova.zelkova.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.zelkova.zelkova.dto.ChatDTO;

@Controller
public class ChatController {

 @MessageMapping("/sendMessage")
 @SendTo("/topic/messages")
 public ChatDTO sendMessage(ChatDTO message) {
  return message; // Message logic can be added
 }
}
