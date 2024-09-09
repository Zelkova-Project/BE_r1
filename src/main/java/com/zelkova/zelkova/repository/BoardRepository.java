package com.zelkova.zelkova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zelkova.zelkova.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
    
}
