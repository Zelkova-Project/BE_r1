package com.zelkova.zelkova.service;

import java.util.Map;

import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;
import com.zelkova.zelkova.dto.PageSearchRequestDTO;
import com.zelkova.zelkova.dto.PageSearchResponseDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface BoardSerivce {
    Long register(BoardDTO boardDTO);

    BoardDTO get(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    Map<String, String> addLike(Long bno);
    
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);

    PageSearchResponseDTO<BoardDTO> findByTitleContainingAndCategory(PageSearchRequestDTO pageSearchRequestDTO);

    PageSearchResponseDTO<BoardDTO> findByContentContainingAndCategory(PageSearchRequestDTO pageSearchRequestDTO);

    PageSearchResponseDTO<BoardDTO> findByContentContaining(PageSearchRequestDTO pageSearchRequestDTO);

    int getBoardCountByEmail(String email);
}



