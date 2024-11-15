package com.zelkova.zelkova.service;

import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface BoardSerivce {
    Long register(BoardDTO boardDTO);

    BoardDTO get(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);
}
