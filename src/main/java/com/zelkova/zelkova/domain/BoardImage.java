package com.zelkova.zelkova.domain;

import jakarta.persistence.Embeddable;
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
@Embeddable
public class BoardImage {
  int ord;

  private String fileName;

  public void setOrd(int ord) {
    this.ord = ord;
  }
}
