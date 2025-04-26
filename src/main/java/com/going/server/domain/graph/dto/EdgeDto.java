package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EdgeDto {
    private String source;
    private String target;
    private String label;

    public static EdgeDto from(String source, String target, String label) {
        return EdgeDto.builder().source(source).target(target).label(label).build();
    }
}
