package com.going.server.domain.graph.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Objects;

@RelationshipProperties
@Getter
@Setter
@Builder
public class GraphEdge {

    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    private String source;

    @EqualsAndHashCode.Include
    private String label;

    @EqualsAndHashCode.Include
    @TargetNode
    private GraphNode target; // 여기에 방향 붙이지 마세요

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphEdge edge)) return false;
        return Objects.equals(source, edge.source) &&
                Objects.equals(label, edge.label) &&
                target != null && edge.target != null &&
                Objects.equals(target.getNodeId(), edge.target.getNodeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, label, target != null ? target.getNodeId() : null);
    }
}
