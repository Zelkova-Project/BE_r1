package com.zelkova.zelkova.repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.domain.BoardImage;
import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;
import com.zelkova.zelkova.service.BoardSerivce;

import jakarta.transaction.Transactional;
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
        for (int i = 0; i <= 10; i++) {
            Board board = Board.builder()
                    .title("테스트 타이틀 " + i)
                    .content("테스트 내용 " + i)
                    .writer("tomhoon" + i)
                    .isDel(false)
                    .date(LocalDate.of(2023, 12, 31))
                    .build();

            board.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE1.jpg");
            board.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE2.jpg");
            board.addCounts();


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
        board.changeDate(LocalDate.of(2024, 9, 10));
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

    @Test
    @Transactional
    public void testRead2() {
        // bno 를 이용해 가져옴
        long id = 10L;

        // 쿼리 한번 실행!
        Optional<Board> result = boardRepository.findById(id);

        // 가져오는 게 없는 경우 처리

        Board board = result.orElseThrow();

        // 이미지 리스트 접근
        // 쿼리 두번 실행!
        List<BoardImage> list = board.getImageList();
        log.info("------ imageList " + list);
    }

    @Test
    public void testRead3() {
        long bno = 1L;
        Optional<Board> result = boardRepository.selectOne(bno);
        Board board = result.orElseThrow();

        log.debug(board);
        log.warn(board);
        log.error(board);
    }

    @Commit
    @Transactional
    @Test
    public void testDel() {
        Long bno = 1L;
        boardRepository.updateToDelete(true, bno);
    }

    @Test
    public void testUpdate() {
        // 글 하나를 가져와서
        Long bno = 10L; // tomhoon9
        Optional<Board> result = boardRepository.selectOne(bno);
        Board board = result.orElseThrow();

        // 이미지 리스트 모두 비우기
        board.clearList();

        // 새로운 이미지 추가하기
        board.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE1.jpg");
        board.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE2.jpg");

        // title, writer, content를 바꾸기
        board.changeContent("Hello!");
        board.changeWriter("tomhoony@@@@@");
        board.changeTitle("using custom set method instead of setter");

        // 저장하기
        boardRepository.save(board);
    }

    @Test
    public void testList2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }

    @Test
    public void testRead4() {
        BoardDTO boardDTO = boardSerivce.get(9L);
        log.info(boardDTO);
    }

}


