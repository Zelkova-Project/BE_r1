package com.zelkova.zelkova.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.zelkova.zelkova.security.filter.JWTCheckFilter;
import com.zelkova.zelkova.security.handler.APILoginFailureHandler;
import com.zelkova.zelkova.security.handler.APILoginSuccessHandler;
import com.zelkova.zelkova.security.handler.CustomAccessDeniedHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@RequiredArgsConstructor
@Log4j2
@EnableMethodSecurity // @PreAuthorize를 위한 설정.(특정 메소드에 권한 확인 후 인가 부여)
public class CustomSecurity {

 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  log.info("-------------security config ----------");

  http.cors(httpSecurityCorsConfigurer -> {
   httpSecurityCorsConfigurer.configurationSource(configurationSource());
  });

  // 요청이 들어올 때 마다 세션 생성을 막음
  http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

  // GET 이외 요청시 "CSRF 토큰"과 함께 요청이 와야함. 지금은 해제
  http.csrf(config -> config.disable());

  // 로그인 경로설정
  // 성공,실패 핸들러 커스텀
  http.formLogin(config -> {
   config.loginPage("/api/member/login");
   config.successHandler(new APILoginSuccessHandler());
   config.failureHandler(new APILoginFailureHandler());
  });

  // JWT 체크할 경로 커스텀 설정
  http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

  // ROLE_ADMIN인데 ROLE_USER가 접근할 경우 에러메세지 던져줄 때 커스텀화
  http.exceptionHandling(config -> {
   config.accessDeniedHandler(new CustomAccessDeniedHandler());
  });

  return http.build();
 }

 @Bean
 public CorsConfigurationSource configurationSource() {
  CorsConfiguration configuration = new CorsConfiguration();

  configuration.setAllowedOriginPatterns(Arrays.asList("*"));
  configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
  configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
  configuration.setAllowCredentials(true);

  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  source.registerCorsConfiguration("/**", configuration);

  return source;
 }

 @Bean
 public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
 }
}
