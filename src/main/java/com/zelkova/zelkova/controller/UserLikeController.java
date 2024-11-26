package com.zelkova.zelkova.controller;

import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.service.UserLikeService;
import com.zelkova.zelkova.util.ApiResponseUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class UserLikeController {
    private final UserLikeService userLikeService;

    @PostMapping("/addBoard/{bno}")
    public ResponseEntity<CommonResponse<Object>> addBoardLike(@PathVariable(name="bno") Long bno) {
        userLikeService.addBoardLike(bno);
        return ApiResponseUtil.success("게시글 번호 " + bno + "의 좋아요가 추가되었습니다.");
    }
    
}


