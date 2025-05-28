package com.going.server.domain.rag.service;

import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.openai.dto.ChatCompletionRequestDto;
import com.going.server.domain.openai.service.OpenAIService;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RagAnswerCreateService {

    private final OpenAIService openAIService;

    private static final String SYSTEM_PROMPT = """
        당신은 초등학생의 이해를 돕는 친절하고 정확한 지식 튜터입니다.
        - 아래 제공된 데이터를 기반으로 질문에 대해 매우 길고 정확하게 설명해주세요.
        - 만약 참고 데이터가 없다면, 교육 도메인의 일반적인 지식을 바탕으로 충실하게 답변해주세요.
        - 반드시 한글로만 응답하고, 인사말이나 불필요한 문장은 생략한 대답만 반환하세요.
        """;

    private static final String MODEL_NAME = "gpt-4-0125-preview";
    private static final double TEMPERATURE = 0.3;
    private static final int MAX_TOKENS = 3000;

    public String chat(List<Chatting> chatHistory, String question) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", SYSTEM_PROMPT));
        messages.add(new ChatMessage("user", question));
        return openAIService.getCompletionResponse(messages, MODEL_NAME, TEMPERATURE, MAX_TOKENS);
    }

    public String chatWithContext(List<Chatting> chatHistory, String finalPrompt) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", SYSTEM_PROMPT));
        messages.add(new ChatMessage("user", finalPrompt));
        return openAIService.getCompletionResponse(messages, MODEL_NAME, TEMPERATURE, MAX_TOKENS);
    }

    private List<ChatMessage> convertHistoryToMessages(List<Chatting> chatHistory) {
        return chatHistory.stream()
                .map(chat -> new ChatMessage(
                        chat.getSender() == Sender.USER ? "user" : "assistant",
                        chat.getContent()
                ))
                .toList();
    }

    public static Chatting ofUser(Graph graph, String content) {
        return Chatting.builder()
                .graph(graph)
                .content(content)
                .sender(Sender.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Chatting ofGPT(Graph graph, String content) {
        return Chatting.builder()
                .graph(graph)
                .content(content)
                .sender(Sender.GPT)
                .createdAt(LocalDateTime.now())
                .build();
    }
}