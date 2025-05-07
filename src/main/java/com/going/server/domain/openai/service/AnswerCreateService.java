package com.going.server.domain.openai.service;

import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.going.server.domain.openai.dto.ChatCompletionRequestDto;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerCreateService {
    private final OpenAiService openAiService;

    // TODO : 프롬프트 튜닝 필요
    // 시스템 역할 설정
    private static final String SYSTEM_PROMPT = """
        당신은 초등학생을 위한 친절한 설명을 제공하는 지식 튜터입니다.
        대답은 짧고 쉽게 설명해주세요. 다른 언급 없이 한글로 대답만을 주세요.
        """;

    // 기존 채팅 이력을 기반으로 GPT 응답 생성
    public String chat(List<Chatting> chatHistory, String question) {

        // 메세지 구성
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", SYSTEM_PROMPT)); // 프롬프트 설정
        messages.addAll(convertHistoryToMessages(chatHistory)); // 기존 채팅
        messages.add(new ChatMessage("user", question)); // 새로운 질문

        // DTO 기반 요청 생성
        ChatCompletionRequestDto request = ChatCompletionRequestDto.builder()
                .model("gpt-4o") // 4o 모델 사용
                .temperature(0.7)
                .maxTokens(500) // 추후 조정 예정
                .messages(messages)
                .build();

        // OpenAI 모델에게 질문 및 응답 생성
        return openAiService.createChatCompletion(request.toRequest())
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }

    private List<ChatMessage> convertHistoryToMessages(List<Chatting> chatHistory) {
        return chatHistory.stream()
                .map(chat -> new ChatMessage(
                        convertSenderToRole(chat.getSender()),
                        chat.getContent()
                ))
                .toList();
    }

    // Chatting 엔티티의 Sender(Enum type) ->  OpenAI 역할 문자열 변환
    private String convertSenderToRole(Sender sender) {
        return sender == Sender.USER ? "user" : "assistant";
    }

    // RAG 컨텍스트 기반 + 기존 채팅 이력을 함께 사용하는 GPT 응답 생성
    public String chatWithContext(List<Chatting> chatHistory, String finalPrompt) {

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", SYSTEM_PROMPT));

        // 기존 대화 이력 추가
        messages.addAll(convertHistoryToMessages(chatHistory));

        // 마지막 질문을 RAG 컨텍스트 기반으로 전달
        messages.add(new ChatMessage("user", finalPrompt));

        ChatCompletionRequestDto request = ChatCompletionRequestDto.builder()
                .model("gpt-4o")
                .temperature(0.7)
                .maxTokens(500)
                .messages(messages)
                .build();

        return openAiService.createChatCompletion(request.toRequest())
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }

}