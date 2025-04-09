package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class graphListDto {
    private List<graphDto> graph;
    public static graphListDto of(List<graphDto> graph) {
        return graphListDto.builder().graph(graph).build();
    }
}
