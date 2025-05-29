package com.going.server.domain.rag.service;

import com.going.server.domain.openai.service.OpenAIService;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// 1. 질문 → Cypher 쿼리 생성 (LLM)
@Component
@RequiredArgsConstructor
public class CypherQueryGenerator {
    private final OpenAIService openAIService;

    public String generate(String userQuestion) {
        String prompt = """
            당신은 Neo4j 그래프 데이터베이스에서 정보를 추출하기 위한 Cypher 쿼리를 생성하는 AI입니다.
            
            - 주어진 질문에서 핵심 개념과 연관된 개념들을 찾아야 합니다.
            - 질문에 포함된 키워드와 의미적으로 밀접한 노드 쌍 간의 관계(triple)를 추출해야 합니다.
            - 반드시 관계 중심 구조 (시작 노드, 관계 라벨, 도착 노드)를 반환하는 Cypher 쿼리를 작성하세요.
            - 관계에 연결된 설명 문장이 있다면 함께 반환하세요. (r.sentence 또는 관계에 걸린 문장)
            - 코드는 반드시 Cypher 쿼리 한 줄만 출력하며, 설명이나 코드블록 없이 순수 쿼리만 출력하세요.

            예시:
            질문: "고래와 관련된 개념들을 알려줘"
            →
            MATCH (a:GraphNode)-[r:RELATED]->(b:GraphNode)
            WHERE toLower(a.label) CONTAINS toLower('고래') OR toLower(b.label) CONTAINS toLower('고래')
            RETURN 
              a.label AS sourceLabel,
              r.label AS relationLabel,
              b.label AS targetLabel,
              r.sentence AS sentence,
              a.label AS nodeLabel
            LIMIT 10

            질문: "%s"
            →
            """.formatted(userQuestion);

        return openAIService.getCompletionResponse(
                List.of(new ChatMessage("user", prompt)),
                "gpt-4o", 0.2, 500
        );
    }
}
