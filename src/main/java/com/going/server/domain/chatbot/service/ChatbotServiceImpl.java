package com.going.server.domain.chatbot.service;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.chatbot.dto.ChatMessage;
import com.going.server.domain.openai.service.AnswerCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotServiceImpl implements ChatbotService {
    private final GraphRepository graphRepository;
    private final
    private final AnswerCreateService answerCreateService;

    // 챗봇 응답 생성
    @Override
    public CreateChatbotResponseDto createAnswer(String graphStrId, CreateChatbotRequestDto createChatbotRequestDto) {
        Long graphId = Long.valueOf(graphStrId);
        // 404 : 지식그래프 찾을 수 없음
        Graph graph = graphRepository.getByGraph(graphId);

        // TODO : RAG 관련 로직 작성

        // 기존 채팅 내역 조회
        List<ChatMessage> chatHistory = new ArrayList<>();
        String question = createChatbotRequestDto.getChatContent();

        // 응답 생성
        String chatContent = answerCreateService.chat(chatHistory, question);

        // response data build
        return CreateChatbotResponseDto.builder()
                .chatContent(chatContent)
                .graphId(graphStrId)
                .createdAt(LocalDateTime.now())
                .retrievedChunks(null)
                .sourceNodes(null)
                .ragMeta(null)
                .build();
    }
}
