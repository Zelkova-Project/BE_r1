package com.zelkova.zelkova.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.CommentDTO;
import com.zelkova.zelkova.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


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
    public Long register(CommentDTO commentDTO) {
        Long cno = commentService.register(commentDTO);
        return cno;
    }
}

