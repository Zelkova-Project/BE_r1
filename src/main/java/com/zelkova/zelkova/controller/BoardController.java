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
import com.zelkova.zelkova.dto.PageSearchRequestDTO;
import com.zelkova.zelkova.dto.PageSearchResponseDTO;
import com.zelkova.zelkova.service.BoardSerivce;
import com.zelkova.zelkova.util.CustomFileUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시글", description = "게시글 API")
public class BoardController {

  private final BoardSerivce boardSerivce;
  private final CustomFileUtil fileUtil;

  @GetMapping("/{bno}")
  public BoardDTO get(@PathVariable(name = "bno") Long bno) {
    return boardSerivce.get(bno);
  }

  // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')") // 권한설정
  // @PreAuthorize("hasRole('ROLE_ADMIN')") // 권한설정
  @GetMapping("/list")
  @Operation(summary = "글리스트 조회", description = "글리스트 조회")
  public Object list(PageRequestDTO pageRequestDTO) {
    
    String option = pageRequestDTO.getSearchOption();

    if (!option.isEmpty()) {
      PageSearchRequestDTO pageSearchRequestDTO = new PageSearchRequestDTO();
      pageSearchRequestDTO.setKeyword(pageRequestDTO.getKeyword());
      pageSearchRequestDTO.setPage(pageRequestDTO.getPage());
      pageSearchRequestDTO.setCategory(pageRequestDTO.getCategory());
      pageSearchRequestDTO.setSize(pageRequestDTO.getSize());

      if (pageRequestDTO.getCategory().equals("community")) {
        pageSearchRequestDTO.setSize(9);
      }

      if (pageSearchRequestDTO.getCategory().equals("")) {
        return searchWithoutCategory(pageSearchRequestDTO);
      }

      if (option.equals("title") ) {
        return searchTitle(pageSearchRequestDTO);
      } else {
        return searchContent(pageSearchRequestDTO);
      }
    } else {
      return defaultList(pageRequestDTO);
    }
  }

  public PageResponseDTO<BoardDTO> defaultList(PageRequestDTO pageRequestDTO) {
    return boardSerivce.list(pageRequestDTO);
  }

  // POST(/api/board/)로 register 등록하기 (리턴은 bno)
  @PostMapping("/")
  public Map<String, Long> register(BoardDTO boardDTO) {
    /**
     * 1. BoardDTO의 물리파일 저장
     * 2. 경로 + 파일명 -> DB에 저장
     */
    // List<MultipartFile> list = boardDTO.getFiles();
    // List<String> uploadFileNames = fileUtil.saveFiles(list);
      List<String> uploadFileNames = boardDTO.getUploadFileNames();

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
    List<MultipartFile> uploadFiles = boardDTO.getFiles();
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

  @PutMapping("/addlike/{bno}")
  public Map<String, String> addLike(@PathVariable(name = "bno") Long bno) {
    return boardSerivce.addLike(bno);
  }

  public PageSearchResponseDTO<BoardDTO> searchTitle(PageSearchRequestDTO pageSearchRequestDTO) {
    PageSearchResponseDTO<BoardDTO> result = boardSerivce.findByTitleContainingAndCategory(pageSearchRequestDTO);
    
    return result;
  }
  
  public PageSearchResponseDTO<BoardDTO> searchContent(PageSearchRequestDTO pageSearchRequestDTO) {
    PageSearchResponseDTO<BoardDTO> result = boardSerivce.findByContentContainingAndCategory(pageSearchRequestDTO);
    
    return result;
  }

  public PageSearchResponseDTO<BoardDTO> searchWithoutCategory(PageSearchRequestDTO pageSearchRequestDTO) {
    PageSearchResponseDTO<BoardDTO> result = boardSerivce.findByContentContaining(pageSearchRequestDTO);
    
    return result;
  }
}





