package com.zelkova.zelkova.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageSearchRequestDTO {

    private String keyword;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}
