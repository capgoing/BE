package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class KnowledgeGraphDto {
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;
    public static KnowledgeGraphDto of(List<NodeDto> nodes, List<EdgeDto> edges) {
        return KnowledgeGraphDto.builder().nodes(nodes).edges(edges).build();
    }
}
