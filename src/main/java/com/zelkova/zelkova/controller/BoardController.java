package com.zelkova.zelkova.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;
import com.zelkova.zelkova.service.BoardSerivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

  private final BoardSerivce boardSerivce;

  @GetMapping("/{bno}")
  public BoardDTO get(@PathVariable(name = "bno") Long bno) {
    return boardSerivce.get(bno);
  }

  @GetMapping("/list")
  public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
    return boardSerivce.list(pageRequestDTO);
  }
}
