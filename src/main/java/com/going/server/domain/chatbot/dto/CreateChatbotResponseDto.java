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
    private String chatContent;                 // 챗봇 응답
    private String graphId;                     // 지식그래프 ID
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;            // 응답 생성 시각
    private List<String> retrievedChunks;       // 후) RAG: 검색된 문장들
    private List<String> sourceNodes;           // 후) RAG: 참조된 지식그래프 노드 ID
    private Map<String, String> ragMeta;        // 후) RAG: 점수, 검색 method 등
}