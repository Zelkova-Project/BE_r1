package com.zelkova.zelkova.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.zelkova.zelkova.domain.Comment;
import com.zelkova.zelkova.dto.CommentDTO;
import com.zelkova.zelkova.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    // @Override
    // public PageResponseDTO<CommentDTO> getList(PageRequestDTO pageRequestDTO) {
    // Pageable pageable = PageRequest
    // .of(0, 10, Sort.by("bno")
    // .descending());

    // Page<Object[]> result = commentRepository.selectList(pageable);

    // List<CommentDTO> dtoList = result.get().map(arr -> {
    // Comment comment = (Comment) arr[0];

    // CommentDTO commentDTO = CommentDTO.builder()
    // .content(comment.getContent())
    // .likes(comment.getLikes())
    // .build();

    // return commentDTO;
    // }).collect(Collectors.toList());

    // return PageResponseDTO.<CommentDTO>withAll()
    // .dtoList(dtoList)
    // .totalCount(10)
    // .pageRequestDTO(pageRequestDTO)
    // .build();
    // }

    private CommentDTO entityToDTO(Comment comment) {
        CommentDTO commentDTO = CommentDTO.builder()
                .cno(comment.getCno())
                .content(comment.getContent())
                .dueDate(comment.getDate())
                .bno(comment.getBoard().getBno())
                .writer(comment.getWriter())
                .build();

        return commentDTO;
    }

    @Override
    public List<CommentDTO> get(Long bno) {
        List<Comment> list = commentRepository.get(bno);
        List<CommentDTO> result = new ArrayList<>();

        for (Comment c : list) {
            CommentDTO commentDTO = entityToDTO(c);
            result.add(commentDTO);
        }

        return result;
    }

    @Override
    public Long register(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getCno();
    }

}
