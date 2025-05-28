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
        너는 Cypher 쿼리 생성기야.
        아래 사용자 질문에 맞는 Cypher 쿼리를 생성해줘.
        데이터는 (:Concept)-[:REL]->(:Concept) 구조야.

        질문: %s

        Cypher 쿼리:
        """.formatted(userQuestion);

        return openAIService.getCompletionResponse(
                List.of(new ChatMessage("user", prompt)),
                "gpt-4-0125-preview", 0.2, 1000
        );
    }
}