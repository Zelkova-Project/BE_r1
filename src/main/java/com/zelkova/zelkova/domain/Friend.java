package com.zelkova.zelkova.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Friend {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long fno;

 private String f_email;

 private String f_nickname;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "email")
 @JsonIgnore
 private Member member;

 @CreatedDate
 private LocalDateTime time;

 public void setMember(Member member) {
  this.member = member;
 }
}
