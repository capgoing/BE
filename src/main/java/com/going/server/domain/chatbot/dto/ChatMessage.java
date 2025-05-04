package com.going.server.domain.chatbot.dto;

import com.going.server.domain.chatbot.entity.Sender;
import lombok.Getter;

@Getter
public class ChatMessage {
    private Sender role;
    private String content;
}
