package com.zelkova.zelkova.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.domain.MemberRole;
import com.zelkova.zelkova.dto.MemberDTO;
import com.zelkova.zelkova.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public MemberDTO getKakaoMember(String accessToken) {
    Map<String, String> map = getEmailFromKakaoAccessToken(accessToken);

    String email = map.get("email");

    Optional<Member> res = memberRepository.findById(email);

    if (res.isPresent()) {
      return entityToMemberDTO(res.get());
    }

    Member member = makeSocialMember(map);
    memberRepository.save(member);

    MemberDTO memberDTO = entityToMemberDTO(member);

    return memberDTO;
  }

  public Map<String, String> getEmailFromKakaoAccessToken(String accessToken) {
    String kakaoURL = "https://kapi.kakao.com/v2/user/me";

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-type", "application/x-www-form-urlencoded");

    HttpEntity entity = new HttpEntity<>(headers);
    UriComponents uriBuilder = UriComponentsBuilder.fromUriString(kakaoURL).build();

    ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
        uriBuilder.toString(),
        HttpMethod.GET,
        entity,
        LinkedHashMap.class);

    LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
    LinkedHashMap<String, String> kakao_account = bodyMap.get("kakao_account");
    log.info("kakao_account : " + kakao_account);

    Map<String, String> result = new HashMap<>();
    result.put("email", kakao_account.get("email"));
    result.put("name", kakao_account.get("name"));

    return result;
  }

  private String makeTempPassword() {
    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i < 10; i++) {
      log.info("Math.random() " + Math.random());
      log.info("Math.random() * 55 " + Math.random() * 55);
      log.info("(Math.random() * 55) + 65 " + (Math.random() * 55) + 65);
      log.info("((int)Math.random() * 55) + 65 " + (int) (Math.random() * 55) + 65);
      log.info("((char)(int)Math.random() * 55) + 65 " + (char) (int) (Math.random() * 55) + 65);

      buffer.append((char) (int) (Math.random() * 55) + 65);
    }

    return buffer.toString();
  }

  private Member makeSocialMember(Map<String, String> map) {
    String email = map.get("email");
    String name = map.get("name");

    String tempPassword = makeTempPassword();

    Member member = Member.builder()
        .email(email)
        .pw(passwordEncoder.encode(tempPassword))
        .nickname(name)
        .isSocial(true)
        .build();

    member.addRole(MemberRole.USER);

    return member;
  }

}
