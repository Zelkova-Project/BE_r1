package com.zelkova.zelkova.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zelkova.zelkova.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

 @Query("select c from Comment c where c.board.bno = :bno")
 List<Comment> get(@Param("bno") Long bno);
}
