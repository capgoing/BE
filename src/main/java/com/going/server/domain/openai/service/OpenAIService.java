package com.going.server.domain.openai.service;

import com.going.server.domain.openai.dto.ChatCompletionRequestDto;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final OpenAiService openAiService;

    public String getCompletionResponse(List<ChatMessage> messages, String model, double temperature, int maxTokens) {
        ChatCompletionRequestDto request = ChatCompletionRequestDto.builder()
                .model(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .messages(messages)
                .build();

        return openAiService.createChatCompletion(request.toRequest())
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}