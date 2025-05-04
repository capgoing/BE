package com.going.server.domain.chatbot.service;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;

public interface ChatbotService {
    // RAG 챗봇 응답 생성
    CreateChatbotResponseDto createAnswer(String graphStrId, CreateChatbotRequestDto createChatbotRequestDto);
}
