package com.zelkova.zelkova.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.MemberDTO;
import com.zelkova.zelkova.service.MemberService;
import com.zelkova.zelkova.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SocialController {

 private final MemberService memberService;

 @GetMapping("/api/member/kakao")
 public Map<String, Object> getMemberFromKakao(String accessToken) {
  log.info("kakao .... ");
  MemberDTO memberDTO = memberService.getKakaoMember(accessToken);

  Map<String, Object> claims = memberDTO.getClaims();

  claims.put("accessToken", JWTUtil.generateToken(claims, 10));
  claims.put("refreshToken", JWTUtil.generateToken(claims, 60 * 24));
  log.info("kakao claims.... " + claims);
  return claims;
 }
}
