package com.going.server.domain.graph.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

@Node("GraphNode")
@Getter
@Setter
public class GraphNode {

    @Id @GeneratedValue
    private Long id;

    private String label;
    private int level;
    private String description;

    @Relationship(type = "HAS_GRAPH", direction = Relationship.Direction.INCOMING)
    private Graph graph;

    // Long → String 변환 (프론트 전송 시)
    public String getIdAsString() {
        return id != null ? String.valueOf(id) : null;
    }
}
