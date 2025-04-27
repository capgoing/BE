package com.going.server.domain.quiz.dto;

import com.going.server.domain.graph.dto.KnowledgeGraphDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ConnectQuizDto {
    private KnowledgeGraphDto knowledgeGraph; // 보여줄 지식 그래프
    private Long questionTargetId; // ? 띄울 노드 id
    private List<String> shuffledOptions; // 문제 리스트
    private String answer; // 정답
}
