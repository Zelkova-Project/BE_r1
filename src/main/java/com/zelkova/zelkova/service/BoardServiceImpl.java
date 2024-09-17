package com.zelkova.zelkova.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;
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

    @Override
    public BoardDTO get(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();
        
        board.changeDate(boardDTO.getDueDate());
        board.changeTitle(boardDTO.getTitle());

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("bno").descending());

        Page<Board> page = boardRepository.findAll(pageable);

        List<BoardDTO> list = 
                page.getContent()
                    .stream()
                    .map(board -> modelMapper.map(board, BoardDTO.class))
                    .collect(Collectors.toList());

        long totalCount = page.getTotalElements();

        PageResponseDTO<BoardDTO> pageResponseDTO = PageResponseDTO.<BoardDTO>withAll()
            .dtoList(list)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(10)
            .build();

        return pageResponseDTO;
    }
}
