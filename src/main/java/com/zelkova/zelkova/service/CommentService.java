package com.zelkova.zelkova.service;

import java.util.List;

import com.zelkova.zelkova.dto.CommentDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface CommentService {

    List<CommentDTO> get(Long bno);

    Long register(CommentDTO commentDTO);
}
