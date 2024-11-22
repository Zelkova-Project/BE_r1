package com.zelkova.zelkova.service;

import jakarta.transaction.Transactional;

@Transactional
public interface UserLikeService {
    public void addBoardLike(Long bno);
}

