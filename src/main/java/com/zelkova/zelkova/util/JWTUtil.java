package com.zelkova.zelkova.util;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTUtil {

  private static String key = "1234567890123456789012345678901234567890";

  public static String generateToken(Map<String, Object> valueMap, int min) {
    SecretKey key = null;

    try {
      key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }

    String jwtStr = Jwts.builder()
        .setHeader(Map.of("typ", "JWT"))
        .setClaims(valueMap) // valueMap이 뭘까
        .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
        .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
        .signWith(key)
        .compact();

    return jwtStr;
  }

  public static Map<String, Object> validateToken(String token) {
    Map<String, Object> claim = null;

    SecretKey key;
    try {
      key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
      claim = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (MalformedJwtException malformedJwtException) {
      throw new CustomJWTException("malformed");
    } catch (ExpiredJwtException expiredJwtException) {
      throw new CustomJWTException("expired");
    } catch (InvalidClaimException invalidClaimException) {
      throw new CustomJWTException("invalidClaimException");
    } catch (JwtException jwtException) {
      throw new CustomJWTException("jwtException");
    } catch (Exception e) {
      throw new CustomJWTException("Error");
    }

    return claim;
  }
}