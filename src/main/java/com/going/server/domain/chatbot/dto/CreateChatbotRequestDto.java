package com.going.server.domain.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateChatbotRequestDto {
    @JsonProperty("isNewChat") // JSON 필드명과 강제 매핑
    private boolean isNewChat; // 새로운 대화인지 여부

    private String chatContent; // 채팅 내용
}