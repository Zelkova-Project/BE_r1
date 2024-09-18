package com.zelkova.zelkova.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.domain.MemberRole;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

 @Autowired
 private MemberRepository memberRepository;

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Test
 public void testInsertMember() {
  for (int i = 0; i < 10; i++) {
   Member member = Member.builder()
     .email("user" + i + "@gmail.com")
     .pw(passwordEncoder.encode("0000"))
     .nickname("nickname" + i)
     .build();

   member.addRole(MemberRole.USER);

   if (i >= 8) {
    member.addRole(MemberRole.MANAGER);
   }

   if (i >= 9) {
    member.addRole(MemberRole.ADMIN);
   }

   memberRepository.save(member);
  }

 }

 @Test
 public void testRead() {
  String email = "user6@gmail.com";

  Member member = memberRepository.getWithRoles(email);

  log.info(member);
 }
}
