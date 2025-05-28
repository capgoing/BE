package com.going.server.domain.rag.service;

import com.going.server.domain.openai.service.OpenAIService;
import com.theokanning.openai.OpenAiService;
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
            당신은 Neo4j용 Cypher 쿼리를 생성하는 AI입니다.
            주어진 질문에 대해 Cypher 쿼리만 반환하세요. 코드블록, 설명 없이 오직 쿼리만 출력해야 합니다.
            
                예:
                질문: "고래와 관련된 개념들을 알려줘"
                → MATCH (n:GraphNode)-[r]->(m:GraphNode)\s
                   WHERE n.label = '고래'\s
                   RETURN m.label AS nodeLabel, m.includeSentence AS sentence\s
                   LIMIT 10
                
                질문: "${userQuestion}"
            →
        """.formatted(userQuestion);

        return openAIService.getCompletionResponse(
                List.of(new ChatMessage("user", prompt)),
                "gpt-4-0125-preview", 0.2, 1000
        );
    }
}