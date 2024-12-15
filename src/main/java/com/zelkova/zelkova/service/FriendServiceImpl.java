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
  public Map<String, String> addFriend(String accessToken, String nickname) {
    Map<String, Object> claims = JWTUtil.validateToken(accessToken);
    String email = (String) claims.get("email");

    Optional<Member> res = memberRepository.findById(email);
    Member 내정보 = res.orElseThrow(() -> new IllegalArgumentException()); // 나

    Optional<Member> friendRes = memberRepository.findByNickname(nickname);
    Member 친구정보 = friendRes.orElseThrow(() -> new IllegalArgumentException());

    List<Friend> list = 내정보.getFriendList();

    for (Friend f : list) {
      String myFriendName = f.getF_nickname();
      if (myFriendName.equals(nickname)) {
        return Map.of("desc", "이미 친추되어 있습니다.", "code", "300");
      }
    }

    Friend friend = Friend.builder()
        .f_email(친구정보.getEmail())
        .f_nickname(친구정보.getNickname())
        .build();

    내정보.addFriend(friend);

    memberRepository.save(내정보);
    return Map.of("desc", "친구 추가가 완료되었습니다.", "code", "100");
  }

  @Override
  public Map<String, String> delFriend(String accessToken, String nickname) {
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
      }
    }

    member.removeFriendList(idx);

    memberRepository.save(member);

    return Map.of("desc", "친구 삭제가 완료되었습니다.", "code", "100");
  }

}
