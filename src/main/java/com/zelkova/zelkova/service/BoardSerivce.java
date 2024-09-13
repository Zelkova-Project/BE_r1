package com.zelkova.zelkova.service;

import com.zelkova.zelkova.dto.BoardDTO;

public interface BoardSerivce {
    Long register(BoardDTO boardDTO);

    BoardDTO get(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);
}
