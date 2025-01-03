package com.zelkova.zelkova.service;

import com.zelkova.zelkova.domain.Member;
import com.zelkova.zelkova.dto.MemberDTO;
import com.zelkova.zelkova.dto.ProfileDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()),
        member.getProfileImageName()
    );
  };

  Map<String, String> saveProfileImage(ProfileDTO profileDTO);

  String getProfileImageName(String email);

  Optional<Member> findByNickname(String writer);

  List<Member> findByEmail(String email);

  void saveMember(Member member);

  boolean checkDuplicateEmail(String email);

  boolean checkDuplicateNickname(String nickname);
}
