package com.zelkova.zelkova.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.domain.Comment;
import com.zelkova.zelkova.dto.CommentDTO;
import com.zelkova.zelkova.service.CommentService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentService commentService;

    @Test
    public void test1() {
        log.info("--------------------");
        log.info(commentRepository);
    }

    @Test
    public void testInsert() {

        Optional<Board> result = boardRepository.findById(1L);
        Board board = result.orElseThrow();

        for (int i = 1; i <= 10; i++) {
            Comment comment = Comment.builder()
                    .content("테스트 댓글 " + i)
                    .isDel(false)
                    .date(LocalDate.of(2023, 12, 31))
                    .board(board)
                    .build();

            commentRepository.save(comment);
        }
    }

    @Test
    public void testDelete() {
        Long cno = 1L;
        commentRepository.deleteById(cno);
    }

    @Test
    public void testRead() {
        Long bno = 1L;

        List<CommentDTO> list = new ArrayList<>();
        list = commentService.get(bno);

        log.info(list);
    }
}

