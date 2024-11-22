package com.zelkova.zelkova.service;

import java.util.List;
import java.util.Map;

import com.zelkova.zelkova.dto.CommentDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface CommentService {

    List<CommentDTO> get(Long bno);

    Long register(CommentDTO commentDTO);

    Map<String, String> addCommentLike(Long id, String email);

    List<List<String>> getLikedUserList(Long id);
}

