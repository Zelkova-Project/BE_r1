package com.zelkova.zelkova.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Data;
import lombok.Builder;

@Data
public class PageResponseDTO<E> {
    
    private List<E> dtoList;

    private List<Integer> pageNumList;

    private PageRequestDTO pageRequestDTO;

    private boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, int totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = totalCount;
        
        // 조회한 내용의 마지막 페이지 계산
        int end = (int)(Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
        int start = end - 9; // 시작 페이지 계산

        // 조회시 1000개, 10개씩 10페이지, 1000 / 10 = 100. 마지막 페이지는 100
        int last = (int)(Math.ceil((totalCount/ (double)pageRequestDTO.getSize())));

        // 10개씩 10페이지씩 조회한 마지막 페이지 = end
        // 데이터 전체의 마지막 페이지 = last
        end = end > last ? last : end;

        // page를 11페이지 조회했을 때 start는 11이다.
        // prev가 존재함. 1 ~ 10
        this.prev = start > 1;

        /// 11페이지조회시 end는 20
        /// 게시글 총 개수는 20 * 10페이지 = 200
        /// 데이터 전체개수 totalCount가 더 많으면 다음 글은 존재
        this.next = totalCount > end * pageRequestDTO.getSize();

        // 11페이지 조회시 
        // start: 11, end: 20
        // pageNumList = [11,12,13,14,15,16,17,18,19,20]
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        if (prev) {
            // 이전페이지 존재시
            // 10을 page에 넣으면 start는 다시 1이되기 때문에
            // 이전 페이지 보기 버튼 클릭시 prevPage를 page로 넘기면 됨
            this.prevPage = start - 1;
        }

        if (next) {
            // 다음 페이지 존재시
            // 21을 page에 넣으면 start는 ceil(21 / 10) * 10 = 30이 되므로
            // 다음 페이지 보기 버튼 클릭시 prevPage에 nextPage를 넘기면 됨
            this.nextPage = end + 1;
        }

        // 전체 페이지
        // IntStream으로 만든 
        // [11,12,13,14... ,20]의 사이즈가 곧 전체 페이지가 됨
        this.totalPage = this.pageNumList.size();
        
        // 현재 페이지
        this.current = pageRequestDTO.getPage();
    }
}

