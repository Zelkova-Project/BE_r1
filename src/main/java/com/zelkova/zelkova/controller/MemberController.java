package com.zelkova.zelkova.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.domain.MemberRole;
import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.service.MemberService;
import com.zelkova.zelkova.util.ApiResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public ResponseEntity<CommonResponse<Object>> joinMember(@RequestBody Map map) {
       // 아이디, 비밀번호, 이름, 생년월일, 이메일
       String id = (String) map.get("login_id"); 
       String password = (String) map.get("password"); 
       String nickname = (String) map.get("name"); 
       String email = (String) map.get("email1") + "@" + (String) map.get("email2");
       
       String info = (String) map.get("info");  // 소개

       Member member = Member.builder()
        .email(id)
        .pw(passwordEncoder.encode(password))
        .nickname(nickname)
        .isSocial(false)
        .profileImageName(null)
        .build();

        member.addRole(MemberRole.USER);
        boolean is닉네임중복 = memberService.checkDuplicateNickname(nickname);

        if (is닉네임중복) {
         return ApiResponseUtil.error("이름이 중복되었습니다.");
        }

        memberService.saveMember(member);

       return ApiResponseUtil.success(Map.of("Success", true));
    }
}

