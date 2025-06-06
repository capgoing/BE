package com.going.server.domain.chatbot.service;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.going.server.domain.chatbot.repository.ChattingRepository;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.exception.GraphContentNotFoundException;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.openai.dto.ImageCreateRequestDto;
import com.going.server.domain.openai.service.ImageCreateService;
import com.going.server.domain.openai.service.SimpleAnswerCreateService;
import com.going.server.domain.openai.service.TextSummaryCreateService;
import com.going.server.domain.rag.service.GraphRAGService;
import com.going.server.domain.rag.service.SimilarityFilterService;
import com.going.server.domain.rag.util.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotServiceImpl implements ChatbotService {
    private final GraphRepository graphRepository;
    private final ChattingRepository chattingRepository;
    private final GraphNodeRepository graphNodeRepository;
    private final SimilarityFilterService similarityFilterService;
    private final PromptBuilder promptBuilder;
    // openai 관련 service
    private final TextSummaryCreateService textSummaryCreateService;
    private final SimpleAnswerCreateService simpleAnswerCreateService;
    private final ImageCreateService imageCreateService;
    // graphRAG
    private final GraphRAGService graphRAGService;

    // 원문 반환
    @Override
    public CreateChatbotResponseDto getOriginalText(String graphId) {
        Long dbId = graphRepository.findDbIdByGraphId(Long.valueOf(graphId));
        Graph graph = graphRepository.getByGraph(dbId);

        return CreateChatbotResponseDto.builder()
                .chatContent(graph.getContent())           // 원문 텍스트
                .graphId(graphId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 요약본 생성
    @Override
    public CreateChatbotResponseDto getSummaryText(String graphId) {
        Long dbId = graphRepository.findDbIdByGraphId(Long.valueOf(graphId));
        Graph graph = graphRepository.getByGraph(dbId);

        String context = Optional.ofNullable(graph.getContent())
                .filter(s -> !s.trim().isEmpty())
                .orElseThrow(GraphContentNotFoundException::new);

        String summary = textSummaryCreateService.summarize(context);

        return CreateChatbotResponseDto.builder()
                .chatContent(summary)                      // 요약 텍스트
                .graphId(graphId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // GraphRAG 챗봇 응답 생성
    @Override
    public CreateChatbotResponseDto createAnswerWithRAG(String graphStrId, CreateChatbotRequestDto requestDto) {
        Long dbId = graphRepository.findDbIdByGraphId(Long.valueOf(graphStrId));
        Graph graph = graphRepository.getByGraph(dbId);

        // 새로운 채팅인 경우
        if (requestDto.isNewChat()) {
            deletePreviousChat(dbId);
        }

        // 새로운 질문 추가
        Chatting userChat = Chatting.builder()
                .graph(graph)
                .content(requestDto.getChatContent())
                .sender(Sender.USER)
                .createdAt(LocalDateTime.now())
                .build();
        chattingRepository.save(userChat);

        // 채팅 내역 조회
        List<Chatting> chatHistory = chattingRepository.findAllByGraphId(dbId);

        // RAG 응답 생성 (응답 + 메타 포함)
        CreateChatbotResponseDto responseDto = graphRAGService.createAnswerWithGraphRAG(
                dbId,
                requestDto.getChatContent(),
                chatHistory
        );

        // 응답 채팅 저장
        Chatting gptChat = Chatting.ofGPT(graph, responseDto.getChatContent());
        chattingRepository.save(gptChat);

        return responseDto;
    }

    // 기본 응답 생성
    @Override
    public CreateChatbotResponseDto createSimpleAnswer(String graphStrId, CreateChatbotRequestDto createChatbotRequestDto) {
        Long dbId = graphRepository.findDbIdByGraphId(Long.valueOf(graphStrId));
        Graph graph = graphRepository.getByGraph(dbId);

        // 새로운 대화인 경우 기존 채팅 삭제
        if (createChatbotRequestDto.isNewChat()) {
            deletePreviousChat(dbId);
        }

        // 기존 채팅 내역 조회
        List<Chatting> chatHistory = chattingRepository.findAllByGraphId(dbId);

        // 사용자 입력 채팅
        String newChat = createChatbotRequestDto.getChatContent();

        // 채팅 저장 (USER)
        Chatting userChat = Chatting.builder()
                .graph(graph)
                .content(newChat)
                .sender(Sender.USER)
                .createdAt(LocalDateTime.now())
                .build();
        chattingRepository.save(userChat);

        // GPT-3.5로 응답 생성 (RAG 없이)
        String chatContent = simpleAnswerCreateService.simpleChat(chatHistory, newChat);

        // 채팅 저장 (GPT)
        Chatting gptChat = Chatting.builder()
                .graph(graph)
                .content(chatContent)
                .sender(Sender.GPT)
                .createdAt(LocalDateTime.now())
                .build();
        chattingRepository.save(gptChat);

        // 응답 반환
        return CreateChatbotResponseDto.builder()
                .chatContent(chatContent)
                .graphId(graphStrId)
                .createdAt(gptChat.getCreatedAt())
                .build();
    }

    /// 4컷만화 생성
    @Override
    public CreateChatbotResponseDto createCartoon(String graphId, CreateChatbotRequestDto createChatbotRequestDto) {
        String concept = createChatbotRequestDto.getChatContent(); // 사용자 질문 → 개념

        // 개선된 프롬프트
        String prompt = """
                당신은 초등학생부터 고등학생까지의 학습자를 위한 교육용 4컷 만화를 그리는 일러스트레이터입니다.
                
                ---
                
                📌 개념: %s
                
                ---
                
                📋 작업 순서:
                
                1. 위에 주어진 개념을 먼저 **충분히 이해**하세요.
                   - 해당 개념의 **의미, 특징, 맥락, 예시**를 상상하고 파악하세요.
                   - 개념에 대한 핵심 요소를 추출하여 시각적으로 설명 가능한 흐름을 구성하세요.
                
                2. 다음 기준에 따라 **한 장의 이미지에 2x2 그리드(총 4컷)**로 그림을 설계하세요.
                   - 왼쪽 위: 장면 1
                   - 오른쪽 위: 장면 2
                   - 왼쪽 아래: 장면 3
                   - 오른쪽 아래: 장면 4
                
                3. **절대 텍스트(한글, 영어, 숫자 등 어떤 형태든 포함 금지)**를 넣지 마세요.
                   - 라벨, 말풍선, 자막, 글자처럼 보일 수 있는 시각 요소도 금지
                   - **오직 시각적 표현만으로** 의미가 전달되어야 합니다
                   - 꼭 해당 개념의 이미지를 가지고 만화를 구성해주세요.
                
                4. 스타일 조건:
                   - **파스텔톤의 부드럽고 따뜻한 색상**
                   - **iOS 이모지 스타일** 또는 **플랫한 웹툰 스타일**
                   - 복잡한 배경 없이 **간단한 배경과 상징**으로 표현
                   - 필요한 경우 **화살표, 흐름선, 상징 기호**를 적절히 사용 (과도하게 사용하지 않음)
                
                ---
                
                🎯 목적:
                글로만 이해하기 어려운 개념을 **누구나 직관적으로 이해할 수 있도록 시각적으로 구성된 4컷 만화로 전달**하는 것입니다.
               """.formatted(concept);

        // DALL-E 이미지 요청 DTO 생성
        ImageCreateRequestDto requestDto = new ImageCreateRequestDto(
                prompt,
                "dall-e-3",
                "vivid",
                "standard",
                "1024x1024",
                1
        );

        // 이미지 생성
        String imageUrl = imageCreateService.generatePicture(requestDto);

        // 응답 DTO 생성
        return CreateChatbotResponseDto.builder()
                .chatContent(imageUrl)
                .graphId(graphId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 추천 영상 생성
    @Override
    public CreateChatbotResponseDto recommendVideo(String graphId, CreateChatbotRequestDto createChatbotRequestDto) {
        return null;
    }

    // 채팅 삭제 메서드
    private void deletePreviousChat(Long graphId) {
        chattingRepository.deleteByGraphId(graphId);
    }

}
