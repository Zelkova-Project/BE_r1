package com.zelkova.zelkova.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO extends User {

  private String email;

  private String pw;

  private String nickname;

  private boolean isSocial;

  private String profileImageName;

  private List<String> roleNames = new ArrayList<>();

  // Security에서의 회원을 다루는 객체인 "User"은 password, username, authority 등이 있다.
  public MemberDTO(String email, String pw, String nickname, boolean isSocial, List<String> roleNames, String profileImageName) {
    super(email, pw,
        roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList()));

    this.email = email;
    this.nickname = nickname;
    this.isSocial = isSocial;
    this.roleNames = roleNames;
    this.profileImageName = profileImageName;
  }

  //
  public Map<String, Object> getClaims() {
    Map<String, Object> dataMap = new HashMap<>();

    dataMap.put("email", email);
    dataMap.put("pw", pw);
    dataMap.put("nickname", nickname);
    dataMap.put("isSocial", isSocial);
    dataMap.put("roleNames", roleNames);
    dataMap.put("profileImageName", profileImageName);

    return dataMap;
  }
  
  public void setProfileImageName(String profileImageName) {
    if (profileImageName.isEmpty()) {
      // profileImageName이 없는 경우
      profileImageName = "https://namu0005.or.kr/api/image/view/default-profile-img.png";
    } else if (!profileImageName.contains("http") && !profileImageName.isEmpty()) {
      // profileImageName이 있지만 파일명만 있는 경우
      profileImageName = "https://namu0005.or.kr/api/image/view/" + profileImageName;
    } 

    this.profileImageName = profileImageName;
  }
}




