package com.zelkova.zelkova.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zelkova.zelkova.domain.Member;

public interface MembeRepository {
 @EntityGraph(attributePaths = { "memberRoleList" })
 @Query("select m from Member m where m.email = :email")
 Member getWithRoles(@Param("email") String email);
}
