package com.zelkova.zelkova.controller;

import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.service.UserLikeService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class UserLikeController {
    private final UserLikeService userLikeService;

    @PostMapping("/addBoard/{bno}")
    public String addBoardLike(@PathVariable(name="bno") Long bno) {
        userLikeService.addBoardLike(bno);
        return null;
    }
    
}

