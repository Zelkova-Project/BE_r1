package com.zelkova.zelkova.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.zelkova.zelkova.domain.Board;
import com.zelkova.zelkova.domain.Comment;
import com.zelkova.zelkova.domain.UserLike;
import com.zelkova.zelkova.dto.CommentDTO;
import com.zelkova.zelkova.dto.UserLikeDTO;
import com.zelkova.zelkova.repository.BoardRepository;
import com.zelkova.zelkova.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
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
            commentDTO.setLikeCount(c.getLikeList().size());

            for(UserLike u : c.getLikeList()) {
                UserLikeDTO userLikeDTO = new UserLikeDTO();
                userLikeDTO.setLno(u.getLno());
                userLikeDTO.setLikedUserEmail(u.getLikedUserEmail());

                commentDTO.addLikeList(userLikeDTO);
            }

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

    @Override
    public Map<String, String> addCommentLike(Long id, String requestEmail) {
        UserLike userLike = new UserLike();
        userLike.setLikedUserEmail(requestEmail);
        
        Optional<Comment> result = commentRepository.findById(id);
        Comment comment = result.orElseThrow();

        // check already liked
        List<UserLike> likedList = comment.getLikeList();

        boolean alreadyLiked = false;
        int targetIdx = 0;

        for (int idx = 0; idx < likedList.size(); idx ++) {
            UserLike u = likedList.get(idx);
            String likedUser = u.getLikedUserEmail();
            if (likedUser != null && likedUser.equals(requestEmail)) {
                alreadyLiked = true;
                targetIdx = idx;
            }
        }

        if (alreadyLiked) {
            comment.removeLikeList(targetIdx);
        } else {
            comment.addLikeList(userLike);
        }

        commentRepository.save(comment);
        
        return Map.of("Result", "Success");
    }

    @Override
    public List<List<String>> getLikedUserList(Long bid) {
        Optional<Board> result = boardRepository.findById(bid);
        Board board = result.orElseThrow();

        List<Comment> list = board.getCommentList();
        List<List<String>> likedUserList = new ArrayList<>();

        for(Comment c : list) {
            List<String> box = new ArrayList<>();

            for (UserLike u : c.getLikeList()) {
                String likedEmail = u.getLikedUserEmail();
                box.add(likedEmail);
            }

            likedUserList.add(box);
        }

        return likedUserList;
    }

    @Override
    public Map<String, String> deleteComment(Long id) {

        Boolean existed = commentRepository.existsById(id);
        
        if (!existed) {
            return Map.of("result", "해당하는 댓글이 없습니다.");
        } else {
            commentRepository.deleteById(id);
            Boolean checkExist = commentRepository.existsById(id);

            String result = checkExist ? "삭제실패" : "삭제성공";

            return Map.of("result", result);
        }
    }

}


