package com.zelkova.zelkova.domain;

import java.time.LocalDate;
import java.util.List;

import com.zelkova.zelkova.dto.CommentDTO;

import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "board")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String writer;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    private boolean isDel;

    private LocalDate date;

    private int counts;

    private int likes;

    // @ElementCollection // 값타입컬렉션 선언
    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> imageList = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLike> userLikeList = new ArrayList<>();
    

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeWriter(String writer) {
        this.writer = writer;
    }

    public void changeIsDel(boolean isDel) {
        this.isDel = isDel;
    }

    public void changeDate(LocalDate date) {
        this.date = date;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void addCounts() {
        this.counts += 1;
    }

    public void addLikes() {
        this.likes += 1;
    }

    public void addImage(BoardImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName) {
        BoardImage boardImage = BoardImage.builder()
                .fileName(fileName)
                .board(this)
                .build();

        addImage(boardImage);
    }
    
    public void addComment(CommentDTO commentDTO) {
        Comment comment = Comment.builder()
            .content(commentDTO.getContent())
            .isDel(commentDTO.isDel())
            .date(commentDTO.getDueDate())
            .likes(commentDTO.getLikes())
            .board(this)
            .build();

        commentList.add(comment);
    }

    public void addUserLike(UserLike userLike) {
        userLikeList.add(userLike);
        userLike.setBoard(this);
    }
    
    public void clearList() {
        this.imageList.clear();
    }
}


