package com.going.server.domain.graph.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;

@RelationshipProperties
@Getter
@Setter
@Builder
public class GraphEdge {

    @Id
    @GeneratedValue
    private Long id; // Neo4j 내부 ID

    private String source;

    private String label; // 관계 라벨

    @TargetNode
    private GraphNode target; // 연결 대상 노드
}
