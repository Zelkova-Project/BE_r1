package com.zelkova.zelkova.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.CommentDTO;
import com.zelkova.zelkova.dto.CommonResponse;
import com.zelkova.zelkova.service.CommentService;
import com.zelkova.zelkova.util.ApiResponseUtil;
import com.zelkova.zelkova.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/comment")
@Log4j2
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{bno}")
    public ResponseEntity<CommonResponse<Object>> get(@PathVariable(name = "bno") Long bno) {
        List<CommentDTO> list = commentService.get(bno);
        return ApiResponseUtil.success(list);
    }

    @PostMapping("/")
    public ResponseEntity<CommonResponse<Object>> register(@RequestBody CommentDTO commentDTO) {
        Long cno = commentService.register(commentDTO);
        return ApiResponseUtil.success(cno);
    }

    @PutMapping("/like/{id}")
    public ResponseEntity<CommonResponse<Object>> addCommentLike(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        // [START] User Identifying 
        String authHeaderStr = authorization;

        String accessToken = authHeaderStr.substring(7);
        
        log.debug(">>>>>>>>authHeaderStr " + authHeaderStr);
        log.debug(">>>>>>>>accessToken " + accessToken);

        Map<String, Object> claims = JWTUtil.validateToken(accessToken);

        String email = (String) claims.get("email");
        // [END] User Identifying 

        // comment id를 받아서 like를 추가함
        commentService.addCommentLike(id, email);
        Map<String, String> result =  Map.of("Result", "Success");
        return ApiResponseUtil.success(result);
    }

    @GetMapping("/likedUserList/{bid}")
    public ResponseEntity<CommonResponse<Object>> getLikedUserList(@PathVariable(name = "bid") Long bid) {
        // board id를 넣어 조회하면
        // board에 존재하는 댓글의 좋아요를 누른 이메일만 뽑아서 리턴

        List<List<String>> result =  commentService.getLikedUserList(bid);
        return ApiResponseUtil.success(result);
    }

    @DeleteMapping("/{bno}")
    public ResponseEntity<CommonResponse<Object>> deleteComment(@PathVariable Long bno) {
        Map<String, String> result = commentService.deleteComment(bno);
        return ApiResponseUtil.success(result);
    }
    
}




