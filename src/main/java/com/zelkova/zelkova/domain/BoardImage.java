package com.zelkova.zelkova.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @Embeddable
@Entity
public class BoardImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long fileNo;

  int ord;

  private String fileName;

  private String filePath;

  @ManyToOne
  @JoinColumn(name = "board_bno", nullable = false)
  private Board board;

  public void setOrd(int ord) {
    this.ord = ord;
  }
}
