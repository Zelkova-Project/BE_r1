package com.zelkova.zelkova.dto;

import lombok.Data;

@Data
public class ChatDTO {
 private String sender;
 private String content;
 private String timestamp;
 private String roomName;
}
