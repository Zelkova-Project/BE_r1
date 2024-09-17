package com.zelkova.zelkova.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
// 소유하고있는 Entity의 특정된 클래스. -> 값 타입 객체
// @Embeddable -> PK생성 안됨
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {
  int ord;

  private String fileName;

  public void setOrd(int ord) {
    this.ord = ord;
  }
}
