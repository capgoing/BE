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
    private Long dbId; // 내부 관리용 elementId와 연결됨

    @Property("id")
    private Long id; // 우리가 직접 사용하는 명시적 ID

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