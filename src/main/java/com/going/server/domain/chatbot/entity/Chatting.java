package com.going.server.domain.chatbot.entity;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.global.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;

@Node("Chatting")
@Getter
@Setter
@NoArgsConstructor
public class Chatting extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Graph graph;  // 연관된 지식그래프 (관계 이름은 자유롭게 설정)

    private String content; // 채팅 내용

    private Sender sender;

    private LocalDateTime createdAt;
}