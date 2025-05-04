package com.going.server.domain.openai.dto;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatCompletionRequestDto {
    private String model;
    private Double temperature;
    private Integer maxTokens;
    private List<ChatMessage> messages;

    // OpenAI 라이브러리 사용을 위해 알맞는 요청 객체로 변환
    public ChatCompletionRequest toRequest() {
        return ChatCompletionRequest.builder()
                .model(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .messages(messages)
                .build();
    }
}