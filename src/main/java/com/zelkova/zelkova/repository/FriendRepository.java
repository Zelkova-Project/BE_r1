package com.zelkova.zelkova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zelkova.zelkova.domain.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
