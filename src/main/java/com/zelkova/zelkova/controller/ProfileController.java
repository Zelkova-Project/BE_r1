package com.zelkova.zelkova.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.dto.ProfileDTO;
import com.zelkova.zelkova.service.BoardSerivce;
import com.zelkova.zelkova.service.MemberService;
import com.zelkova.zelkova.util.ApiResponseUtil;
import com.zelkova.zelkova.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final BoardSerivce boardSerivce;

    private final MemberService memberService;

    @GetMapping("/userinfo/{email}")
    public ResponseEntity<CommonResponse<Object>> getUserInfo(@PathVariable(name = "email") String email) {

        int 게시글개수 = boardSerivce.getBoardCountByEmail(email);

        Map<Object, Object> result = new HashMap<>();

        result.put("프로필사진", "");
        result.put("게시글개수", 게시글개수);
        result.put("아는친구", 0);
        result.put("친한친구", 0);

        return ApiResponseUtil.success(result);
    }

    @GetMapping("/getProfile")
    @Operation(summary = "프로필 전체 조회", description = "프로필 전체 조회")
    public ResponseEntity<CommonResponse<ProfileDTO>> getProfile(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        Map<String, Object> claims = JWTUtil.validateToken(token);

        String nickname = (String) claims.get("nickname");
        String email = (String) claims.get("email");

        // ! 토큰에 담긴 이미지명은 최초 이미지명이라 변경한 내용을 못 잡는다.
        String profileImageName = memberService.getProfileImageName(email);

        ProfileDTO profileDTO = ProfileDTO.builder()
            .nickname(nickname)
            .email(email)
            .profileImageName(profileImageName)
            .build();
            
        return ApiResponseUtil.success(profileDTO);
    }

    @PostMapping("/saveProfileImage")
    @Operation(summary = "프로필사진저장", description = "사진명만 넣으면 됨")
    public ResponseEntity<CommonResponse<Map<String, String>>> saveProfileImage(
            @RequestHeader("Authorization") String authorization, 
            @RequestBody ProfileDTO profileDTO)
    {
        String token = authorization.substring(7);
        Map<String, Object> claims = JWTUtil.validateToken(token);
        
        String nickname = (String) claims.get("nickname");
        String email = (String) claims.get("email");

        profileDTO.setNickname(nickname);
        profileDTO.setEmail(email);

        Map<String, String> result = memberService.saveProfileImage(profileDTO);

        return ApiResponseUtil.success(result);
    }
}



