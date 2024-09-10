package com.zelkova.zelkova.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardSerivce{

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO boardDTO) {
        /**
         * DB에 넣기 위해서는 순수성(영속성)을 띄고 있는 객체로 변환해야함
         * 1. ModelMapper로 domain를 DTO와 매핑시켜 가져온다 
         * 2. domain을 DB에 넣어준다. 
         */
        Board board = modelMapper.map(boardDTO, Board.class);
        Board savedBoard = boardRepository.save(board);

        return savedBoard.getBno();
    }
    
}
