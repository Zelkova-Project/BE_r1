package com.zelkova.zelkova.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Data;
import lombok.Builder;

@Data
public class PageSearchResponseDTO<T> {

    private List<T> dtoList;

    private List<Integer> pageNumList;

    private PageSearchRequestDTO pageSearchRequestDTO;

    private boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageSearchResponseDTO(List<T> dtoList, PageSearchRequestDTO pageSearchRequestDTO, int totalCount) {
        this.dtoList = dtoList;
        this.pageSearchRequestDTO = pageSearchRequestDTO;
        this.totalCount = totalCount;
        
        int page = pageSearchRequestDTO.getPage(); // 현재페이지
        int size = pageSearchRequestDTO.getSize();

        int totalPage = (int)Math.ceil(totalCount / size);

        int blockSize = (int)Math.ceil(page / 10.0);
        int start = (blockSize - 1) * 10 + 1;

        int end = (int)Math.ceil(page/10.0) * 10;

        if (totalPage < end) {
            end = totalPage;
        }
        if (end == 1) {
            end = 2;
        }
        this.totalPage = totalPage;

        boolean prev = start > 10;
        boolean next = totalCount > ((end + 1) * 10);

        this.prev = prev;
        this.next = next;
        this.current = page;
        
        List<Integer> pageNumList = IntStream.range(start, end + 1).boxed().collect(Collectors.toList());
        this.pageNumList = pageNumList;
        
        this.totalCount = totalCount;

        if (prev) {
            this.prevPage = start - 1;
        } 

        if (next) {
            this.nextPage = end + 1;
        }
    }
}





