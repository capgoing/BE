package com.going.server.domain.openai.service;

import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.going.server.domain.openai.dto.ChatCompletionRequestDto;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleAnswerCreateService {

    private final OpenAiService openAiService;

    // 단순한 챗봇 응답 역할 프롬프트
    private static final String SIMPLE_SYSTEM_PROMPT = """
        당신은 일반적인 잡담 챗봇입니다.
        너무 전문적인 정보는 피하고, 간단하고 짧은 응답을 한국어로 해주세요.
        최대한 가볍고 단순하게 대답하세요.
        """;

    // GPT-3.5 기반 단순 응답 생성 메서드
    public String simpleChat(List<Chatting> chatHistory, String userInput) {
        List<ChatMessage> messages = new ArrayList<>();

        // 역할 지정
        messages.add(new ChatMessage("system", SIMPLE_SYSTEM_PROMPT));

        // 기존 채팅 이력
        for (Chatting chat : chatHistory) {
            String role = convertSenderToRole(chat.getSender());
            messages.add(new ChatMessage(role, chat.getContent()));
        }

        // 새로운 질문 추가
        messages.add(new ChatMessage("user", userInput));

        // 요청 객체 생성
        ChatCompletionRequestDto request = ChatCompletionRequestDto.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.9)
                .maxTokens(300)
                .messages(messages)
                .build();

        return openAiService.createChatCompletion(request.toRequest())
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }

    private String convertSenderToRole(Sender sender) {
        return sender == Sender.USER ? "user" : "assistant";
    }
}
