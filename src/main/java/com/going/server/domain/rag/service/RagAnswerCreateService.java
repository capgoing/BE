package com.going.server.domain.rag.service;

import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.going.server.domain.openai.service.OpenAIService;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 4. GraphRAG 응답 생성
@Service
@RequiredArgsConstructor
public class RagAnswerCreateService {

    private final OpenAIService openAIService;

    private static final String SYSTEM_PROMPT = """
    당신은 초등학생의 이해를 돕는 친절하고 정확한 지식 튜터입니다.

    - 아래에 제공된 '관계 정보'와 '설명 문장'은 질문과 관련된 지식그래프에서 추출된 정보입니다.
    - 반드시 이 정보를 바탕으로 질문에 대해 정확하고 구체적으로 설명해주세요.
    - 관계 간의 연결 흐름이나 개념 간 연관성을 쉽게 풀어 설명해 주세요.
    - 필요 이상으로 친절하거나 장황하게 말하지 말고, 정확하고 알기 쉽게 대답만 하세요.
    - 대답은 반드시 한글로만 작성하고, 인사말이나 부가 설명 없이 본문만 반환하세요.
    """;

    private static final String MODEL_NAME = "gpt-4o";
    private static final double TEMPERATURE = 0.3;
    private static final int MAX_TOKENS = 1200;

    public String chat(List<Chatting> chatHistory, String question) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", SYSTEM_PROMPT));
        messages.addAll(convertHistoryToMessages(chatHistory));
        messages.add(new ChatMessage("user", question));
        return openAIService.getCompletionResponse(messages, MODEL_NAME, TEMPERATURE, MAX_TOKENS);
    }

    public String chatWithContext(List<Chatting> chatHistory, String finalPrompt) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", SYSTEM_PROMPT));
        messages.addAll(convertHistoryToMessages(chatHistory));
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
}