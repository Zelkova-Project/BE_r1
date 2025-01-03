package com.zelkova.zelkova.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_bno")
    @JsonIgnore
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_cno")
    @JsonIgnore
    private Comment comment;

    private String likedUserEmail;

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void setLikedUserEmail(String email) {
        this.likedUserEmail = email;
    }
}


