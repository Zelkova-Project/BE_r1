package com.zelkova.zelkova.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long cno;

    private String content;

    private boolean isDel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    
    private Long bno;

    private String writer;

    private String profileImageName;

    @Builder.Default
    private List<UserLikeDTO> likeList = new ArrayList<>();

    private int likeCount;

    public void addLikeList(UserLikeDTO userLikeDTO) {
        this.likeList.add(userLikeDTO);
    }
}



