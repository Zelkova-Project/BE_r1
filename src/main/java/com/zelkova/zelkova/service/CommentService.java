package com.zelkova.zelkova.service;

import java.util.List;

import com.zelkova.zelkova.dto.CommentDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface CommentService {
    
    List<CommentDTO> get(Long bno);

    PageResponseDTO<CommentDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(CommentDTO commentDTO);
}

