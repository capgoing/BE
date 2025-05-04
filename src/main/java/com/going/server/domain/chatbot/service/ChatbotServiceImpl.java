package com.going.server.domain.chatbot.service;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.repository.GraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotServiceImpl implements ChatbotService {
    private final GraphRepository graphRepository;

    // 챗봇 응답 생성
    @Override
    public CreateChatbotResponseDto createAnswer(String graphStrId, CreateChatbotRequestDto createChatbotRequestDto) {
        Long graphId = Long.valueOf(graphStrId);
        // 404 : 지식그래프 찾을 수 없음
        Graph graph = graphRepository.getByGraph(graphId);


        return null;
    }
}
