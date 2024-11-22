package com.zelkova.zelkova.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.domain.UserLike;
import com.zelkova.zelkova.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserLikeServiceImpl implements UserLikeService{
    
    private final BoardRepository boardRepository;

    @Override
    public void addBoardLike(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        UserLike userLike = UserLike.builder().build();

        board.addUserLike(userLike);

        boardRepository.save(board);
    }
    
}

