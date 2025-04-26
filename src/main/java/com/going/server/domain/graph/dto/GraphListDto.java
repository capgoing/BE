package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GraphListDto {
    private List<GraphDto> graph;
    public static GraphListDto of(List<GraphDto> graph) {
        return GraphListDto.builder().graph(graph).build();
    }
}
