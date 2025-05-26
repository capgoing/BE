package com.going.server.domain.chatbot.service;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;

public interface ChatbotService {
    // 원문 반환
    String getOriginalText(String graphId);
    // 요약본 생성
    String getSummaryText(String graphId);
    // RAG 챗봇 응답 생성
    CreateChatbotResponseDto createAnswerWithRAG(String graphStrId, CreateChatbotRequestDto createChatbotRequestDto);
    // RAG 사용하지 않는 응답 생성
    CreateChatbotResponseDto createSimpleAnswer(String graphId, CreateChatbotRequestDto createChatbotRequestDto);
    // 4컷만화 생성
    CreateChatbotResponseDto createCartoon(String graphId, CreateChatbotRequestDto createChatbotRequestDto);
    // 추천 영상 생성
    CreateChatbotResponseDto recommendVideo(String graphId, CreateChatbotRequestDto createChatbotRequestDto);
}
