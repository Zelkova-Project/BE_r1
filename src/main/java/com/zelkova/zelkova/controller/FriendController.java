package com.zelkova.zelkova.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.domain.Friend;
import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.service.FriendService;
import com.zelkova.zelkova.util.ApiResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

  private FriendService friendService;

  public FriendController(FriendService friendSerivce) {
    this.friendService = friendSerivce;
  }

  @GetMapping("/get")
  @Operation(summary = "친구목록 조회", description = "친구목록 조회")
  public ResponseEntity<CommonResponse<List<Friend>>> getFriends(@RequestHeader("Authorization") String authorization) {
    String authHeaderStr = authorization;

    String accessToken = authHeaderStr.substring(7);
    List<Friend> list = friendService.getFriends(accessToken);

    return ApiResponseUtil.success(list);
  }

  @PostMapping("/add")
  @Operation(summary = "친구목록 추가", description = "친구목록 추가")
  public ResponseEntity<CommonResponse<Map<String, String>>> addFriend(
      @RequestHeader("Authorization") String authorization,
      @RequestBody String nickname) {
    String authHeaderStr = authorization;

    String accessToken = authHeaderStr.substring(7);

    return ApiResponseUtil.success(friendService.addFriend(accessToken, nickname));
  }

  @DeleteMapping("/del/{nickname}")
  @Operation(summary = "친구목록 삭제", description = "친구목록 삭제")
  public ResponseEntity<CommonResponse<Map<String, String>>> delFriend(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String nickname) {
    String authHeaderStr = authorization;

    String accessToken = authHeaderStr.substring(7);

    return ApiResponseUtil.success(friendService.delFriend(accessToken, nickname));
  }

}
