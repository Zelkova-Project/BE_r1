package com.zelkova.zelkova.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

  private final MemberService memberService;

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @GetMapping("/rooms")
  @Operation(summary = "현재 갖고 있는 채팅방 조회", description = "현재 갖고 있는 채팅방 조회")
  public List<Map<String, Object>> getRooms() {
    Set<String> rooms = stringRedisTemplate.keys("chatRoom:*:messages");

    List<Map<String, Object>> result = new ArrayList<>();

    if (rooms == null || rooms.isEmpty()) {
      return new ArrayList<>();
    }

    // member들의 프로필정보 넣어주기
    for (String room : rooms) {
      String 방참여자들 
        = room.replace("chatRoom:/topic/", "")
              .replace(":messages", "");

      if (방참여자들.contains(",")) {
        // 유저들 정보를 삽입
        String[] list = 방참여자들.split(",");

        for (String name : list) {
          Map<String, Object> 정보 = new HashMap<String, Object>();
          List<Object> 참여유저정보리스트 = new ArrayList<>();

          Member member = memberService.findByNickname(name).orElseThrow(() -> new IllegalArgumentException("올바르지 않는 nickname"));
          
          Map<String, Object> memberMap = new HashMap<>();
          memberMap.put("email", member.getEmail());
          memberMap.put("nickname", member.getNickname());
          memberMap.put("profileImageName", member.getProfileImageName());
          memberMap.put("friendList", member.getFriendList());

          참여유저정보리스트.add(memberMap);
          
          정보.put("방제", 방참여자들); // ex) TOMHOON,JOHN
          정보.put("유저", 참여유저정보리스트);

          result.add(정보);
        }
      
      } else {
        // ?: 방제가 잘못 들어온 경우 방어코드용
        Map<String, Object> 유저없는정보 = new HashMap<>();
        유저없는정보.put("방제", 방참여자들);
        유저없는정보.put("유저", List.of());

        result.add(유저없는정보);
      }
    }


    return result;
  }
}


