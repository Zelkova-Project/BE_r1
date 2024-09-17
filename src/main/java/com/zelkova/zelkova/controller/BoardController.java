package com.zelkova.zelkova.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  // POST(/api/board/)로 register 등록하기 (리턴은 bno)
  @PostMapping("/")
  public Map<String, Long> register(@RequestBody BoardDTO boardDTO) {
    long bno = boardSerivce.register(boardDTO);
    return Map.of("bno", bno);
  }

  // PUT 수정({bno}) queryString으로 bno 받아서 dto를 만들어서 수정하기 (리턴은 SUCCESS)
  @PutMapping("/{bno}")
  public Map<String, String> modify(@PathVariable(name = "bno") Long bno, @RequestBody BoardDTO boardDTO) {
    boardDTO.setBno(bno);

    boardSerivce.modify(boardDTO);

    return Map.of("RESULT", "SUCCESS");
  }
  
  // DELETE 삭제({tno}) queryString으로 bno 받아서 삭제 처리하기 
  @DeleteMapping("/{bno}")
  public Map<String, String> remove(@PathVariable(name = "bno") Long bno) {
    boardSerivce.remove(bno);
    return Map.of("RESULT", "SUCCESS");
  }
}
