package com.zelkova.zelkova.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zelkova.zelkova.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
    @EntityGraph(attributePaths = "imageList")
    // attributePath는 '값 타입 객체'로 선언된 필드값을 join 대상 테이블로 잡아준다
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> selectOne(@Param("bno") Long bno);

    @Modifying
    @Query("update Board b set b.isDel = :flag where b.bno = :bno")
    void updateToDelete(@Param("flag") boolean flag, @Param("bno") Long bno);

    @Query("select b, bi from Board b left join b.imageList bi where bi.ord = 0 and b.isDel = false")
    Page<Object[]> selectList(Pageable pagebale);
}
