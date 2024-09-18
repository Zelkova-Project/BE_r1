package com.zelkova.zelkova.controller;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;
import com.zelkova.zelkova.service.BoardSerivce;
import com.zelkova.zelkova.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

  private final BoardSerivce boardSerivce;
  private final CustomFileUtil fileUtil;

  @GetMapping("/{bno}")
  public BoardDTO get(@PathVariable(name = "bno") Long bno) {
    return boardSerivce.get(bno);
  }

  @GetMapping("/list")
  public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
    log.info("test");
    return boardSerivce.list(pageRequestDTO);
  }

  // POST(/api/board/)로 register 등록하기 (리턴은 bno)
  @PostMapping("/")
  public Map<String, Long> register(BoardDTO boardDTO) {
    /**
     * 1. BoardDTO의 물리파일 저장
     * 2. 경로 + 파일명 -> DB에 저장
     */
    List<MultipartFile> list = boardDTO.getImageList();
    List<String> uploadFileNames = fileUtil.saveFiles(list);

    boardDTO.setUploadFileNames(uploadFileNames);

    long bno = boardSerivce.register(boardDTO);

    return Map.of("bno", bno);
  }

  // PUT 수정({bno}) queryString으로 bno 받아서 dto를 만들어서 수정하기 (리턴은 SUCCESS)
  @PutMapping("/{bno}")
  public Map<String, String> modify(@PathVariable(name = "bno") Long bno, BoardDTO boardDTO) {
    boardDTO.setBno(bno);

    BoardDTO oldDTO = boardSerivce.get(bno);

    // 새로올린 이미지들 업로드
    List<MultipartFile> uploadFiles = boardDTO.getImageList();
    List<String> uploadFileNames = fileUtil.saveFiles(uploadFiles);

    boardSerivce.modify(boardDTO);

    // 기존에 올라갔던 이미지들
    List<String> existFileNames = boardDTO.getUploadFileNames();
    uploadFileNames.addAll(existFileNames); // 새로운이미지 + 기존이미지 = DB에들어갈데이터

    List<String> oldFileNames = oldDTO.getUploadFileNames();

    if (oldFileNames.size() != 0 || oldFileNames != null) {
      List<String> deleteFiles = oldFileNames.stream().filter(old -> uploadFileNames.indexOf(old) == -1)
          .collect(Collectors.toList());
      fileUtil.deleteFiles(deleteFiles);
    }

    return Map.of("RESULT", "SUCCESS");
  }

  // DELETE 삭제({tno}) queryString으로 bno 받아서 삭제 처리하기
  @DeleteMapping("/{bno}")
  public Map<String, String> remove(@PathVariable(name = "bno") Long bno) {
    List<String> oldFileNames = boardSerivce.get(bno).getUploadFileNames();
    fileUtil.deleteFiles(oldFileNames);
    boardSerivce.remove(bno);
    return Map.of("RESULT", "SUCCESS");
  }
}
