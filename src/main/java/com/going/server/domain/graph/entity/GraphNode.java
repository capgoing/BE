package com.going.server.domain.graph.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Node("GraphNode")
@Getter
@Setter
@Builder
public class GraphNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property("node_id")
    private Long nodeId;

    private String label;
    private String group;
    private Long level;
    private String includeSentence; //해당 노드(단어)가 포함된 문장

//    @Relationship(type = "HAS_GRAPH", direction = Relationship.Direction.INCOMING)
//    private Graph graph;

    @Relationship(type = "RELATED", direction = Relationship.Direction.OUTGOING)
    private Set<GraphEdge> edges;

    public String getIdAsString() {
        return id != null ? String.valueOf(id) : null;
    }
}