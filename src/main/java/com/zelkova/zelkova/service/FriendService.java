package com.zelkova.zelkova.service;

import java.util.List;

import com.zelkova.zelkova.domain.Friend;

import jakarta.transaction.Transactional;

@Transactional
public interface FriendService {

 public List<Friend> getFriends(String accessToken);

 public void addFriend(String accessToken, String nickname);

 public void delFriend(String accessToken, String nickname);
}
