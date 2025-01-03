package com.zelkova.zelkova.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    private String content;

    private boolean isDel;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "board_bno", nullable = true)
    @JsonIgnore
    private Board board;

    @ManyToOne
    @JoinColumn(name = "writer", referencedColumnName = "nickname", nullable = true)
    private Member member;
    
    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLike> likeList = new ArrayList<>();

    public void addLikeList(UserLike userLike) {
        userLike.setComment(this);
        this.likeList.add(userLike);
    }

    public void removeLikeList(int idx) {
        this.likeList.remove(idx);
    }

    public String getWriter() {
        return this.member != null ? member.getNickname() : null;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}




