package com.going.server.domain.graph.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;

@RelationshipProperties
@Getter
@Setter
public class GraphEdge {

    @Id
    @GeneratedValue
    private Long id; // Neo4j 내부 ID

    private String label; // 관계 라벨

    @TargetNode
    private GraphNode target; // 연결 대상 노드

    @Property
    private Long edgeId;  // JSON에서 받은 숫자형 edge ID (ex. 12)

    private GraphEdge createEdge(Long edgeId, String label, GraphNode source, GraphNode target) {
        GraphEdge edge = new GraphEdge();
        edge.setEdgeId(edgeId);
        edge.setLabel(label);
        edge.setTarget(target);

        // outbound edge를 source에 연결
        if (source.getEdges() == null) {
            source.setEdges(new ArrayList<>());
        }
        source.getEdges().add(edge);
        return edge;
    }

    // Long → String 변환 (프론트 전송 시)
    public String getIdAsString() {
        return id != null ? String.valueOf(id) : null;
    }

}
