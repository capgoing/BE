package com.going.server.global.response;

/**
 * 게시글 등의 리스트 조회 시 필요한 응답 클래스
 * Pageable 이용
 */
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PageResponse<T> {

    private int totalPage;
    private Long totalElements;
    private int pagingSize;
    private int currentPage;
    private Boolean isFirst;
    private Boolean isLast;
    private Boolean isEmpty;
    private List<T> data;

    public static PageResponse of(Page page) {
        return PageResponse.builder()
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pagingSize(page.getSize())
                .currentPage(page.getNumber() + 1)
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .isEmpty(page.isEmpty())
                .data(page.getContent())
                .build();
    }
}
