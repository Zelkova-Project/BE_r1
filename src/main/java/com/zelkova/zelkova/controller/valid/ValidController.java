package com.zelkova.zelkova.controller.valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.service.MemberService;
import com.zelkova.zelkova.util.ApiResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/valid")
@RequiredArgsConstructor
@Log4j2
public class ValidController {
    
    private final MemberService memberService;

    @GetMapping("/existMember")
    public ResponseEntity<CommonResponse<Object>> existMember(String email) {
        List<Member> list = memberService.findByEmail(email);

        if (list.size() != 0) {
            return ApiResponseUtil.success(Map.of("possible", false));
        } else {
            return ApiResponseUtil.success(Map.of("possible", true));
        }
    }
    
}

