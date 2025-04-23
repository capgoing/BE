package com.going.server.domain.graph.entity;

import com.going.server.global.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("KnowledgeGraph")
@Getter
@Setter
public class Graph extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id; //그래프 id
    private String title; // 그래프 제목
    private boolean listenUpPerfect; //listenUp 퀴즈 만접 여부
    private boolean connectPerfect; //connect 퀴즈 만접 여부
    private boolean picturePerfect; //picture 퀴즈 만접 여부
}