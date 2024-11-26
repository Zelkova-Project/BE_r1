package com.zelkova.zelkova.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.service.BoardSerivce;
import com.zelkova.zelkova.util.ApiResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final BoardSerivce boardSerivce;

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
}


