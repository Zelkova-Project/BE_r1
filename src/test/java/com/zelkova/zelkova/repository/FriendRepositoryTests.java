package com.zelkova.zelkova.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zelkova.zelkova.domain.Friend;
import com.zelkova.zelkova.domain.Member;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class FriendRepositoryTests {

 @Autowired
 private FriendRepository friendRepository;

 @Autowired
 private MemberRepository memberRepository;

 @Test
 @Transactional
 public void test1() {
  Optional<Member> res = memberRepository.findById("user0@gmail.com");
  Optional<Member> res1 = memberRepository.findById("user1@gmail.com");
  Member member = res.orElseThrow(() -> new IllegalArgumentException());
  Member 탐훈 = res1.orElseThrow(() -> new IllegalArgumentException());

  Friend friend = new Friend();
  friend.setMember(member);

  탐훈.addFriend(friend);

  memberRepository.save(탐훈);

  Optional<Member> res탐훈 = memberRepository.findById("user1@gmail.com");
  Member res탐훈1 = res탐훈.orElseThrow(() -> new IllegalArgumentException());
  log.debug("res탐훈1 >>> " + res탐훈1.getFriendList());
 }
}
