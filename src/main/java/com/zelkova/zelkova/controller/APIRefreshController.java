package com.zelkova.zelkova.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.util.CustomJWTException;
import com.zelkova.zelkova.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class APIRefreshController {

 @RequestMapping("/api/member/refresh")
 public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
  if (refreshToken == null) {
   throw new CustomJWTException("NULL_REFRESH");
  }

  if (authHeader == null || authHeader.length() < 7) {
   throw new CustomJWTException("INVALID_STRING");
  }

  String accessToken = authHeader.substring(7);

  // ACCESS TOKEN 살아있으면 그대로 리턴
  if (checkExpiredToken(accessToken) == false) {
   return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
  }

  // refresh check
  Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

  String newAccessToken = JWTUtil.generateToken(claims, 10);

  // refreshToken 만료가 1시간도 안남았으면 새로발급
  String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60 * 24)
    : refreshToken;

  return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
 }

 private boolean checkTime(Integer exp) {
  java.util.Date expDate = new java.util.Date((long) exp * (1000));
  long gap = expDate.getTime() - System.currentTimeMillis();

  long leftMin = gap / (1000 * 60);

  return leftMin < 60;
 }

 private boolean checkExpiredToken(String token) {
  try {
   JWTUtil.validateToken(token);

  } catch (CustomJWTException e) {
   if (e.getMessage().equals("Expired")) {
    return true;
   }
  }

  return false;
 }

}
