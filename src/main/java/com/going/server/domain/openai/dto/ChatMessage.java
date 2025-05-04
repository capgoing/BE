package com.going.server.domain.openai.dto;

import lombok.Getter;

@Getter
public class ChatMessage {
    private String role;
    private String content;
}
