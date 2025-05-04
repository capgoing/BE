package com.going.server.domain.openai.service;

import com.going.server.domain.openai.dto.ChatCompletionRequestDto;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.internal.messaging.Message;
import org.springframework.stereotype.Service;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerCreateService {
    @Resource(name = "getOpenAIService")
    private final OpenAiService openAiService;
    private String prompt;

    public String chat(List<Message> chatHistory, String question) {
        ChatCompletionRequestDto request = ChatCompletionRequestDto.builder()
                .messages(messages) // 이전 대화 목록 리스트
                .prompt(prompt)     // 프롬프트
                .newQuestion(question)
                .model("gpt-4o") // 사용 모델
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
