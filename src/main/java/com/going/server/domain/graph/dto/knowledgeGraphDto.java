package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class knowledgeGraphDto {
    private List<nodeDto> nodes;
    private List<edgeDto> edges;
    public static knowledgeGraphDto of(List<nodeDto> nodes, List<edgeDto> edges) {
        return knowledgeGraphDto.builder().nodes(nodes).edges(edges).build();
    }
}
