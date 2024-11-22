package com.zelkova.zelkova.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.zelkova.zelkova.dto.ChatDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController {
	
  private final SimpMessagingTemplate template;


 @MessageMapping("/sendMessage/{roomName}")
 @SendTo("/topic/{roomName}")
 public ChatDTO sendMessage(ChatDTO message) {
   return message; // Message logic can be added
 }
 
 @MessageMapping("/deliver")
public void deliver(ChatDTO chatDTO) {
  template.convertAndSend("/topic/" + chatDTO.getRoomName(), chatDTO);
}

 @MessageMapping("/enter")
 public void sending(ChatDTO chatDTO) {
   log.info(">>>>>>");
  template.convertAndSend("/topic/" + chatDTO.getRoomName(), chatDTO);
 }
}
