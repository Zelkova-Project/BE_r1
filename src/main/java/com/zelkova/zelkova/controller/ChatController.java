package com.zelkova.zelkova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.zelkova.zelkova.dto.ChatDTO;
import com.zelkova.zelkova.service.redis.ChatPublisher;
import com.zelkova.zelkova.service.redis.ChatSubscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController {
	
  private final SimpMessagingTemplate template;


    @Autowired
    private ChatPublisher chatPublisher;

    @Autowired
    private ChatSubscriber chatSubscriber;

    // Endpoint to send a message to a chat room
    @MessageMapping("/deliver")
    public void sendMessage(ChatDTO chatDTO) {
        // template.convertAndSend("/topic/" + chatDTO.getRoomName(), chatDTO);

        chatPublisher.sendMessage("/topic/" + chatDTO.getRoomName(), chatDTO);
    }

    @MessageMapping("/joinChatRoom")  
    public void joinChatRoom(String chatRoomId) {
        chatSubscriber.sendChatHistory(chatRoomId, 10);  // Send the last 10 messages from the chat room
    }



//  @MessageMapping("/sendMessage/{roomName}")
//  @SendTo("/topic/{roomName}")
//  public ChatDTO sendMessage2(ChatDTO message) {
//    return message; // Message logic can be added
//  }
 
//  @MessageMapping("/deliver")
// public void deliver(ChatDTO chatDTO) {
//   template.convertAndSend("/topic/" + chatDTO.getRoomName(), chatDTO);
// }

//  @MessageMapping("/enter")
//  public void sending(ChatDTO chatDTO) {
//    log.info(">>>>>>");
//   template.convertAndSend("/topic/" + chatDTO.getRoomName(), chatDTO);
//  }
}