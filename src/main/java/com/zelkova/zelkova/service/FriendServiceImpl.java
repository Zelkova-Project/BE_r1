package com.zelkova.zelkova.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zelkova.zelkova.domain.Friend;
import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.repository.MemberRepository;
import com.zelkova.zelkova.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class FriendServiceImpl implements FriendService {

 private final MemberRepository memberRepository;

 @Override
 public List<Friend> getFriends(String accessToken) {
  Map<String, Object> claims = JWTUtil.validateToken(accessToken);
  String email = (String) claims.get("email");

  Optional<Member> res = memberRepository.findById(email);
  Member member = res.orElseThrow(() -> new IllegalArgumentException());
  return member.getFriendList();
 }

 @Override
 public void addFriend(String accessToken, String nickname) {
  Map<String, Object> claims = JWTUtil.validateToken(accessToken);
  String email = (String) claims.get("email");

  Optional<Member> res = memberRepository.findById(email);
  Member member = res.orElseThrow(() -> new IllegalArgumentException());

  Optional<Member> friendRes = memberRepository.findByNickname(nickname);
  Member 친구정보 = friendRes.orElseThrow(() -> new IllegalArgumentException());

  Friend friend = new Friend();
  friend.setMember(member);

  친구정보.addFriend(friend);

  memberRepository.save(친구정보);

  log.debug("friend >>> " + friend);
 }

 @Override
 public void delFriend(String accessToken, String nickname) {
  Map<String, Object> claims = JWTUtil.validateToken(accessToken);
  String email = (String) claims.get("email");

  Optional<Member> res = memberRepository.findById(email);
  Member member = res.orElseThrow(() -> new IllegalArgumentException());

  List<Friend> list = member.getFriendList();

  int idx = 0;

  for (int i = 0; i < list.size(); i++) {
   boolean isSame = list.get(i).getMember().getNickname().equals(nickname);
   if (isSame) {
    idx = i;
    return;
   }
  }

  member.removeFriendList(idx);

  memberRepository.save(member);
 }

}
