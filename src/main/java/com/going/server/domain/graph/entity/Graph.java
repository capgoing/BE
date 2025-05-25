package com.going.server.domain.graph.entity;

import com.going.server.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@Node("Graph")
@Getter
@Setter
@Builder
public class Graph extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id; //그래프 id -> 프론트와 통신에서는 String 값으로 사용

    private String title;

    // DB에 content 추가
    private String content;

    private boolean listenUpPerfect; //listenUp 퀴즈 만접 여부
    private boolean connectPerfect; //connect 퀴즈 만접 여부
    private boolean picturePerfect; //picture 퀴즈 만접 여부

    @Relationship(type = "HAS_NODE", direction = Relationship.Direction.OUTGOING)
    private List<GraphNode> nodes;

    // Long → String 변환 (프론트 전송 시)
    public String getIdAsString() {
        return id != null ? String.valueOf(id) : null;
    }
}