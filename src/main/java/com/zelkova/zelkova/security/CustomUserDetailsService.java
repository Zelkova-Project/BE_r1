package com.zelkova.zelkova.security;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.dto.MemberDTO;
import com.zelkova.zelkova.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
 private final MemberRepository memberRepository;

 // Security Config에서 설정한 .formLogin에서의 경로에 Post로 던지면 이곳에 도착한다. (흐름은 Security
 // Context 구조특징임)
 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  log.info("---------------loadUserByUserName");

  Member member = memberRepository.getWithRoles(username);

  if (member == null) {
   throw new UsernameNotFoundException("Not Found");
  }

  MemberDTO memberDTO = new MemberDTO(
    member.getEmail(),
    member.getPw(),
    member.getNickname(),
    member.isSocial(),
    member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));

  memberDTO.setProfileImageName(member.getProfileImageName());

  log.info("memberDTO ::: " + memberDTO);
  return memberDTO;
 }

}

