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

// RAG 대화
@Service
@RequiredArgsConstructor
public class RAGAnswerCreateService {
    private final OpenAiService openAiService;

    // 시스템 역할 설정
    private static final String SYSTEM_PROMPT = """
    당신은 초등학생의 이해를 돕는 친절하고 정확한 지식 튜터입니다.
    - 아래 제공된 데이터를 기반으로 질문에 대해 매우 길고 정확하게 설명해주세요.
    - 만약 참고 데이터가 없다면, 교육 도메인의 일반적인 지식을 바탕으로 충실하게 답변해주세요.
    - 반드시 한글로만 응답하고, 인사말이나 불필요한 문장은 생략한 대답만 반환하세요.
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
                .model("gpt-4-0125-preview")
                .temperature(0.3)
                .maxTokens(3500)
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
                .model("gpt-4-0125-preview")
                .temperature(0.3)
                .maxTokens(3500)
                .messages(messages)
                .build();

        return openAiService.createChatCompletion(request.toRequest())
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}