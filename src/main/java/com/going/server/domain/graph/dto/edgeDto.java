package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class edgeDto {
    private String source;
    private String target;
    private String label;

    public static edgeDto from(String source, String target, String label) {
        return edgeDto.builder().source(source).target(target).label(label).build();
    }
}
