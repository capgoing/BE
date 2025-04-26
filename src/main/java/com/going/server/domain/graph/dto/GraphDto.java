package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GraphDto {
    private Long id;
    private String title;
    private Boolean easy;
    private Boolean hard;

    public static GraphDto of(Long id, String title, Boolean easy, Boolean hard) {
        return GraphDto.builder().id(id).title(title).easy(easy).hard(hard).build();
    }
}
