package com.zelkova.zelkova.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.CommentDTO;
import com.zelkova.zelkova.service.CommentService;
import com.zelkova.zelkova.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

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
    public List<CommentDTO> get(@PathVariable(name = "bno") Long bno) {
        List<CommentDTO> list = commentService.get(bno);
        return list;
    }

    @PostMapping("/")
    public Long register(@RequestBody CommentDTO commentDTO) {
        Long cno = commentService.register(commentDTO);
        return cno;
    }

    @PutMapping("/like/{id}")
    public Map<String, String> addCommentLike(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        // [START] User Identifying 
        String authHeaderStr = authorization;

        String accessToken = authHeaderStr.substring(7);
        Map<String, Object> claims = JWTUtil.validateToken(accessToken);

        String email = (String) claims.get("email");
        // [END] User Identifying 

        // comment id를 받아서 like를 추가함
        commentService.addCommentLike(id, email);
        return Map.of("Result", "Success");
    }

    @GetMapping("/likedUserList/{bid}")
    public List<List<String>> getLikedUserList(@PathVariable(name = "bid") Long bid) {
        // board id를 넣어 조회하면
        // board에 존재하는 댓글의 좋아요를 누른 이메일만 뽑아서 리턴

        return commentService.getLikedUserList(bid);
    }
    
}

