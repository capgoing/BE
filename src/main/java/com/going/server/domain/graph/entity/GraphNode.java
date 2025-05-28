package com.going.server.domain.graph.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
    private String image;

    @ToString.Exclude
    @JsonIgnore
    @Relationship(type = "RELATED", direction = Relationship.Direction.OUTGOING)
    private Set<GraphEdge> edges;
}