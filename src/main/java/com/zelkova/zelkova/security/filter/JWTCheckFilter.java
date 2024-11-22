package com.zelkova.zelkova.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.zelkova.zelkova.dto.MemberDTO;
import com.zelkova.zelkova.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    // 필터지정하지 않을 녀석들 선언
    String path = request.getRequestURI();

    if (request.getMethod().equals("OPTIONS")) {
      return true;
    }

    log.info("check uri ............ " + path);

    if (path.startsWith("/api/member/")) {
      return true;
    }

    if (path.startsWith("/api/image/")) {
      return true;
    }

    if (path.startsWith("/api/files/")) {
      return true;
    }

    if (path.startsWith("/api/chat-websocket/")) {
      return true;
    }

    if (path.startsWith("/api/board/")) {
     return true;
    }

    if (path.startsWith("/api/comment/")) {
      return true;
     }

     if (path.startsWith("/swagger-ui/")) {
      return true;
     }
     if (path.startsWith("/v3/")) {
      return true;
     }

    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeaderStr = request.getHeader("Authorization");

    try {
      String accessToken = authHeaderStr.substring(7);
      Map<String, Object> claims = JWTUtil.validateToken(accessToken);

      log.info("JWT claims " + claims);

      String email = (String) claims.get("email");
      String pw = (String) claims.get("pw");
      String nickname = (String) claims.get("nickname");
      Boolean isSocial = (Boolean) claims.get("isSocial");
      List<String> roleNames = (List<String>) claims.get("roleNames");

      MemberDTO memberDTO = new MemberDTO(email, pw, nickname, isSocial.booleanValue(), roleNames);

      log.info("------------------------");
      log.info(memberDTO);
      log.info("memberDTO.getAuthorities : " + memberDTO.getAuthorities());

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO,
          memberDTO.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      ;

      filterChain.doFilter(request, response);

    } catch (Exception e) {
      log.error("JWT ERROR");

      Gson gson = new Gson();
      String msg = gson.toJson(Map.of("ERROR", "ERROR_ACCESS_TOKEN"));

      response.setContentType("application/json");
      PrintWriter printWriter = response.getWriter();
      printWriter.println(msg);
      printWriter.close();
    }
  }
}


