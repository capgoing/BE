package com.going.server.domain.openai.service;

import com.going.server.domain.chatbot.dto.ChatCompletionRequestDto;
import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class {

    @Resource(name = "getOpenAIService")
    private final OpenAiService openAiService;

    public String chat(List<Chatting> chatHistory, String question) {
        String systemPrompt = """
                당신은 초등학생을 위한 친절한 설명을 제공하는 지식 튜터입니다.
                대답은 짧고 쉽게 설명해주세요.
                """;

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", systemPrompt));
        messages.addAll(convertHistoryToMessages(chatHistory));
        messages.add(new ChatMessage("user", question));

        ChatCompletionRequestDto request = ChatCompletionRequestDto.builder()
                .messages(messages)
                .model("gpt-4o")
                .build();

        return openAiService.createChatCompletion(request)
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

    private String convertSenderToRole(Sender sender) {
        return sender == Sender.USER ? "user" : "assistant";
    }
}
