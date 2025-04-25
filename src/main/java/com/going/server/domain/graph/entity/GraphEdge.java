package com.going.server.domain.graph.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@Setter
public class GraphEdge {

    @Id
    @GeneratedValue
    private Long id;

    private String label;

    @TargetNode
    private GraphNode target;

    @Property
    private String edgeId;  // 원래 JSON의 "id" 필드 보관용
}