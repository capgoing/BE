package com.going.server.domain.graph.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.Objects;

@RelationshipProperties
@Getter
@Setter
@Builder
public class GraphEdge {

    @Id
    @GeneratedValue
    private Long id; // Neo4j 내부 ID

    @EqualsAndHashCode.Include
    private String source;

    @EqualsAndHashCode.Include
    private String label; // 관계 라벨

    @EqualsAndHashCode.Include
    @TargetNode
    @Relationship(type = "RELATED", direction = Relationship.Direction.INCOMING)
    @JsonIgnore
    private GraphNode target; // 연결 대상 노드

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphEdge edge = (GraphEdge) o;
        return Objects.equals(source, edge.source)
                && Objects.equals(label, edge.label)
                && edge.target != null && target != null
                && Objects.equals(target.getNodeId(), edge.target.getNodeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, label, target != null ? target.getNodeId() : null);
    }

}
