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
    private List<String> retrievedChunks;
    private List<String> sourceNodes;
    private Map<String, String> ragMeta;

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
                .retrievedChunks(retrievedChunks)
                .sourceNodes(sourceNodes)
                .ragMeta(Map.of("chunkCount", String.valueOf(retrievedChunks.size())))
                .build();
    }
}