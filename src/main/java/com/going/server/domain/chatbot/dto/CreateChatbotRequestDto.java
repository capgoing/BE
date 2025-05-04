package com.going.server.domain.chatbot.dto;

import lombok.Getter;

@Getter
public class CreateChatbotRequestDto {
    private boolean isNewChat; // 새로운 대화인지 여부
    private String chatContent; // 채팅 내용
}