package com.zelkova.zelkova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zelkova.zelkova.domain.UserLike;

public interface UserLikeRepository extends JpaRepository<UserLike, Long>{
    
}

