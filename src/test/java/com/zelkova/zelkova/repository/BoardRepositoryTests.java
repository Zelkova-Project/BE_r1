package com.zelkova.zelkova.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;
import com.zelkova.zelkova.service.BoardSerivce;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardSerivce boardSerivce;

    @Test
    public void test1() {
        log.info("--------------------");
        log.info(boardRepository);
    }

    @Test
    public void testRead() {
        Long bno = 1L;
        List<Board> result = boardRepository.findAll();
        log.info(result);
        System.out.println(result);
    }

    @Test
    public void testInsert() {
        for (int i = 0; i <= 10; i ++) {
            Board board = Board.builder()
            .title("테스트 타이틀 " + i)
            .writer("tomhoon" + i)
            .isDel(false)
            .date(LocalDate.of(2023, 12,31))
            .build();

            boardRepository.save(board);
        }

    }

    @Test
    public void testModify() {
        Long bno = 15L;
        Optional<Board> result = boardRepository.findById(bno);

        if (!result.isPresent()) {
            return;
        }

        Board board = result.orElseThrow();

        board.changeTitle("Today is Tuesday");
        board.changeDate(LocalDate.of(2024,9,10));
        board.changeWriter("tomhoon ch1");
        board.changeIsDel(false);

        boardRepository.save(board);
    }

    @Test
    public void testDelete() {
        Long bno = 1L;
        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        
        Page<Board> result = boardRepository.findAll(pageable);

        log.info(result.getTotalElements());

        result.getContent().stream().forEach(board -> log.info(board));
    }

    @Test
    public void testGet() {
        Long bno = 10L;

        BoardDTO boardDTO = boardSerivce.get(bno);
        log.info(boardDTO);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .size(10)
            .page(2)
            .build();
        
        PageResponseDTO<BoardDTO> pageResponseDTO = boardSerivce.list(pageRequestDTO);

        log.info(pageResponseDTO);
    }
}
