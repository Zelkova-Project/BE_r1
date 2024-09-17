package com.zelkova.zelkova.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String content;

    private boolean isDel;

    private LocalDate date;

    @ElementCollection // 값타입컬렉션 선언
    @Builder.Default
    private List<BoardImage> imageList = new ArrayList<>();

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

    public void addImage(BoardImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName) {
        BoardImage boardImage = BoardImage.builder()
                .fileName(fileName)
                .build();

        addImage(boardImage);
    }

    public void clearList() {
        this.imageList.clear();
    }

}
