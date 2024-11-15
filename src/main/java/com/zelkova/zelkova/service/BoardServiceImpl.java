package com.zelkova.zelkova.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.domain.BoardImage;
import com.zelkova.zelkova.domain.UserLike;
import com.zelkova.zelkova.dto.BoardDTO;
import com.zelkova.zelkova.dto.PageRequestDTO;
import com.zelkova.zelkova.dto.PageResponseDTO;
import com.zelkova.zelkova.dto.PageSearchRequestDTO;
import com.zelkova.zelkova.dto.PageSearchResponseDTO;
import com.zelkova.zelkova.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardSerivce {

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
        List<MultipartFile> list = boardDTO.getFiles();

        for (MultipartFile file : list) {
            board.addImageString(file.getOriginalFilename());
        }

        Board savedBoard = boardRepository.save(board);

        return savedBoard.getBno();
    }

    @Override
    public BoardDTO get(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        BoardDTO boardDTO = entityToDTO(board);

        return boardDTO;
    }

    private BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .dueDate(board.getDate())
                .uploadFileNames(board.getUploadFileNames())
                .likeList(board.getUserLikeList())
                .build();

        List<BoardImage> list = board.getImageList();
        if (list.size() == 0)
            return boardDTO;

        List<String> fileList = list.stream().map(boardImage -> boardImage.getFileName()).toList();

        boardDTO.setUploadFileNames(board.getUploadFileNames());

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();

        board.clearList();

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
        board.changeDate(boardDTO.getDueDate());

        List<String> uploadList = boardDTO.getUploadFileNames();
        if (uploadList.size() != 0 || uploadList != null) {
            uploadList.stream().forEach(fileName -> {
                board.addImageString(fileName);
            });
        }

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.updateToDelete(true, bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("bno").descending());

        Page<Board> page = boardRepository.findAll(pageable);

        List<BoardDTO> list = page.getContent()
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

    @Override
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest
                .of(0, 10, Sort.by("bno")
                        .descending());
        Page<Object[]> result = boardRepository.selectList(pageable);

        List<BoardDTO> dtoList = result.get().map(arr -> {
            Board board = (Board) arr[0];
            BoardImage boardImage = (BoardImage) arr[1];

            BoardDTO boardDTO = BoardDTO.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getWriter())
                    .build();

            String imageStr = boardImage.getFileName();
            boardDTO.setUploadFileNames(List.of(imageStr));
            return boardDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<BoardDTO>withAll()
                .dtoList(dtoList)
                .totalCount(10)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Map<String, String> addLike(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        UserLike userLike = UserLike.builder().build();

        board.addUserLike(userLike);
        boardRepository.save(board);

        return Map.of("RESULT", "SUCCESS");
    }

    @Override
    public PageSearchResponseDTO<BoardDTO> findByTitleContaining(PageSearchRequestDTO pageSearchRequestDTO) {
        int page = pageSearchRequestDTO.getPage();
        int size = pageSearchRequestDTO.getSize();
        String keyword = pageSearchRequestDTO.getKeyword();
        
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("bno").descending());

        Page<Board> 제목포함리스트 = boardRepository.findByTitleContaining(keyword, pageable);
        
        List<BoardDTO> list = 제목포함리스트.getContent()
                    .stream()
                    .map(item -> modelMapper.map(item, BoardDTO.class))
                    .collect(Collectors.toList());

        PageSearchResponseDTO<BoardDTO> pageSearchResponseDTO 
            = PageSearchResponseDTO.<BoardDTO>withAll()
                .dtoList(list)
                .pageSearchRequestDTO(pageSearchRequestDTO)
                .totalCount(list.size())
                .build();

        return pageSearchResponseDTO;
    }

    @Override
    public PageSearchResponseDTO<BoardDTO> findByContentContaining(PageSearchRequestDTO pageSearchRequestDTO) {
        int page = pageSearchRequestDTO.getPage();
        int size = pageSearchRequestDTO.getSize();
        String keyword = pageSearchRequestDTO.getKeyword();
        
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("bno").descending());

        Page<Board> 내용포함리스트 = boardRepository.findByContentContaining(keyword, pageable);
        
        List<BoardDTO> list = 내용포함리스트.getContent()
                    .stream()
                    .map(item -> modelMapper.map(item, BoardDTO.class))
                    .collect(Collectors.toList());

        PageSearchResponseDTO<BoardDTO> pageSearchResponseDTO 
            = PageSearchResponseDTO.<BoardDTO>withAll()
                .dtoList(list)
                .pageSearchRequestDTO(pageSearchRequestDTO)
                .totalCount(list.size())
                .build();

        return pageSearchResponseDTO;
    }
    
}


