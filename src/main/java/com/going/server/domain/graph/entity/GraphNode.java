package com.going.server.domain.graph.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("GraphNode")
@Getter
@Setter
public class GraphNode {

    @Id
    @GeneratedValue
    private Long id;

    private String label;
    private int level;
    private String description;

    @Relationship(type = "HAS_GRAPH", direction = Relationship.Direction.INCOMING)
    private Graph graph;

    @Relationship(type = "RELATED", direction = Relationship.Direction.OUTGOING)
    private List<GraphEdge> edges = new ArrayList<>();

    public String getIdAsString() {
        return id != null ? String.valueOf(id) : null;
    }
}