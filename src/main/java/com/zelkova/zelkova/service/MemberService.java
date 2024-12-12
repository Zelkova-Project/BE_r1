package com.zelkova.zelkova.service;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.dto.MemberDTO;

import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

@Transactional
public interface MemberService {
  MemberDTO getKakaoMember(String accessToken);

  default MemberDTO entityToMemberDTO(Member member) {
    return new MemberDTO(
        member.getEmail(),
        member.getPw(),
        member.getNickname(),
        member.isSocial(),
        member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));
  };
}
