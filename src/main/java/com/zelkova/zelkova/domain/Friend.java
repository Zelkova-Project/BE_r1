package com.zelkova.zelkova.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Friend {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long fno;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "email")
 private Member member;

 @CreatedDate
 private LocalDateTime time;

 public void setMember(Member member) {
  this.member = member;
 }
}
