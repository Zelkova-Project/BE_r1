package com.zelkova.zelkova.domain;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
public class Member {
 @Id
 private String email;

 private String pw;

 private String nickname;

 private boolean isSocial;

 private String profileImageName;

 // 값 타입 객체. 테이블로 따로 관리. LazyLoading 형식(접근 필요없으면 쿼리x)
 @ElementCollection(fetch = FetchType.LAZY)
 @Builder.Default
 private List<MemberRole> memberRoleList = new ArrayList<>();

 @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
 private List<Friend> friendList = new ArrayList<>();

 public void addRole(MemberRole memberRole) {
  memberRoleList.add(memberRole);
 }

 public void clearRole() {
  memberRoleList.clear();
 }

 public void changeNickname(String nickname) {
  this.nickname = nickname;
 }

 public void changePw(String pw) {
  this.pw = pw;
 }

 public void changeSocial(boolean isSocial) {
  this.isSocial = isSocial;
 }

 public void addFriend(Friend friend) {
  this.friendList.add(friend);
  friend.setMember(this);
 }

 public void removeFriendList(int idx) {
  this.friendList.remove(idx);
 }

 public void setProfileImageName(String profileImageName) {
    this.profileImageName = profileImageName;
 }
}

