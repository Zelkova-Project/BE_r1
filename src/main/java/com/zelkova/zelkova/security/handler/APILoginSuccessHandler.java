package com.zelkova.zelkova.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.google.gson.Gson;
import com.zelkova.zelkova.dto.MemberDTO;
import com.zelkova.zelkova.util.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    log.info("---------------------------success handler start");
    log.info(authentication);
    log.info("---------------------------success handler finish");

    MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
    Map<String, Object> claims = memberDTO.getClaims();

    String accessToken = JWTUtil.generateToken(claims, 120); // 2hours
    String refreshToken = JWTUtil.generateToken(claims, 60 * 24); // 24 hours

    claims.put("accessToken", accessToken);
    claims.put("refreshToken", refreshToken);
    // claims.put("accessToken", "accessToken");
    // claims.put("refreshToken", "refreshToken");

    Gson gson = new Gson();

    String jsonStr = gson.toJson(claims);

    response.setContentType("application/json; charset=UTF-8");
    PrintWriter printWriter = response.getWriter();
    printWriter.println(jsonStr);
    printWriter.close();
  }

}
