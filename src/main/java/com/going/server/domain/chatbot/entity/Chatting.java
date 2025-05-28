package com.going.server.domain.chatbot.entity;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;

@Node("Chatting")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Chatting {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Graph graph;  // 연관된 지식그래프 (관계 이름은 자유롭게 설정)

    private String content; // 채팅 내용

    private Sender sender;

    private LocalDateTime createdAt;

    public static Chatting ofUser(Graph graph, String content) {
        return Chatting.builder()
                .graph(graph)
                .content(content)
                .sender(Sender.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Chatting ofGPT(Graph graph, String content) {
        return Chatting.builder()
                .graph(graph)
                .content(content)
                .sender(Sender.GPT)
                .createdAt(LocalDateTime.now())
                .build();
    }
}