package com.going.server.domain.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class CreateChatbotResponseDto {
    private String chatContent;
    private String graphId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;
    private List<String> retrievedTriples; //관계 중심의 3요소 표현 ("물 -상태변화→ 응고")
    private List<String> sourceNodes; //질의에 사용된 핵심 노드들 ("물", "응고" 등)
    private List<String> 증강할때쓴자료; //LLM에 넘긴 context 문장들 (이름은 `augmentedSentences` 등으로 변경 권장)

    private Map<String, String> ragMeta; //(ex: 사용한 쿼리문 등)

    public static CreateChatbotResponseDto of(
            String chatContent,
            String graphId,
            LocalDateTime createdAt,
            List<String> retrievedChunks,
            List<String> sourceNodes
    ) {
        return CreateChatbotResponseDto.builder()
                .chatContent(chatContent)
                .graphId(graphId)
                .createdAt(createdAt)
                .retrievedTriples(retrievedChunks)
                .sourceNodes(sourceNodes)
                .ragMeta(Map.of("chunkCount", String.valueOf(retrievedChunks.size())))
                .build();
    }
}