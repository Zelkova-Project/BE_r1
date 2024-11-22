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
public class PageRequestDTO {
    
    @Builder.Default
    private String category = "";

    @Builder.Default
    private String searchOption = "";

    @Builder.Default
    private String keyword = "";

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}

