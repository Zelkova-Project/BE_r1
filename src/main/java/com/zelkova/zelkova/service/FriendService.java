package com.zelkova.zelkova.service;

import java.util.List;
import java.util.Map;

import com.zelkova.zelkova.domain.Friend;

import jakarta.transaction.Transactional;

@Transactional
public interface FriendService {

 public List<Friend> getFriends(String accessToken);

 public Map<String, String> addFriend(String accessToken, String nickname);

 public Map<String, String> delFriend(String accessToken, String nickname);
}
