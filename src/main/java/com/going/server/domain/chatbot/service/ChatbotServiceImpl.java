package com.going.server.domain.chatbot.service;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.going.server.domain.chatbot.repository.ChattingRepository;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.GraphContentNotFoundException;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.openai.dto.ImageCreateRequestDto;
import com.going.server.domain.openai.service.ImageCreateService;
import com.going.server.domain.openai.service.RAGAnswerCreateService;
import com.going.server.domain.openai.service.SimpleAnswerCreateService;
import com.going.server.domain.openai.service.TextSummaryCreateService;
import com.going.server.domain.rag.service.SimilarityFilterService;
import com.going.server.domain.rag.util.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

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
    private final RAGAnswerCreateService ragAnswerCreateService;
    private final ImageCreateService imageCreateService;

    // 원문 반환
    @Override
    public CreateChatbotResponseDto getOriginalText(String graphId) {
        Graph graph = graphRepository.getByGraph(Long.valueOf(graphId));

        return CreateChatbotResponseDto.builder()
                .chatContent(graph.getContent())           // 원문 텍스트
                .graphId(graphId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 요약본 생성
    @Override
    public CreateChatbotResponseDto getSummaryText(String graphId) {
        Graph graph = graphRepository.getByGraph(Long.valueOf(graphId));

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


    // RAG 챗봇 응답 생성
    @Override
    public CreateChatbotResponseDto createAnswerWithRAG(String graphStrId, CreateChatbotRequestDto createChatbotRequestDto) {
        Long graphId = Long.valueOf(graphStrId);

        // 404 : 지식그래프 찾을 수 없음
        Graph graph = graphRepository.getByGraph(graphId);

        // RAG: 사용자 질문
        String userQuestion = createChatbotRequestDto.getChatContent();

        // RAG: 유사 노드 검색 및 문장 추출
        List<GraphNode> matchedNodes = graphNodeRepository.findByKeyword(userQuestion);
        List<String> candidateSentences = matchedNodes.stream()
                .map(GraphNode::getIncludeSentence)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // RAG: 유사 문장 필터링
        List<String> filteredChunks = similarityFilterService.filterRelevantSentences(userQuestion, candidateSentences);

        // RAG: 최종 프롬프트 구성
        String finalPrompt = promptBuilder.buildPrompt(filteredChunks, userQuestion);

        // RAG: 메타정보 수집
        List<String> retrievedChunks = new ArrayList<>(filteredChunks);
        List<String> sourceNodes = new ArrayList<>(
                matchedNodes.stream().map(GraphNode::getLabel).distinct().toList()
        );
        Map<String, String> ragMeta = Map.of(
                "chunkCount", String.valueOf(filteredChunks.size())
        );

        // 새로운 대화인 경우 기존 채팅 삭제
        if (createChatbotRequestDto.isNewChat()) {
            deletePreviousChat(graphId);
        }

        // 기존 채팅 내역 조회
        List<Chatting> chatHistory = chattingRepository.findAllByGraphId(graphId);

        // 새로운 채팅
        String newChat = createChatbotRequestDto.getChatContent();

        // 새로운 채팅 repository에 저장
        Chatting chatting = Chatting.builder()
                .graph(graph)
                .content(newChat)
                .sender(Sender.USER)
                .createdAt(LocalDateTime.now())
                .build();
        chattingRepository.save(chatting);

        // 응답 생성
        String chatContent;

        // RAG: 유사 문장이 있을 경우 컨텍스트 활용
        if (retrievedChunks.isEmpty()) {
            System.out.println("[INFO] RAG 미적용 - 일반 채팅 기반 응답");
            System.out.println("[INFO] RAG 미적용 - 유사 문장 없음");
            System.out.println("[DEBUG] matchedNodes.size(): " + matchedNodes.size());
            System.out.println("[DEBUG] candidateSentences.size(): " + candidateSentences.size());
            System.out.println("[DEBUG] filteredChunks.size(): " + filteredChunks.size());
            chatContent = ragAnswerCreateService.chat(chatHistory, newChat);
        } else {
            System.out.println("[INFO] RAG 적용됨 - 유사 문장 " + retrievedChunks.size() + "개 포함");
            chatContent = ragAnswerCreateService.chatWithContext(chatHistory, finalPrompt);
        }

        // 응답 repository에 저장
        Chatting answer = Chatting.builder()
                .graph(graph)
                .content(chatContent)
                .sender(Sender.GPT)
                .createdAt(LocalDateTime.now())
                .build();
        chattingRepository.save(answer);

        // 반환
        return CreateChatbotResponseDto.builder()
                .chatContent(chatContent)
                .graphId(graphStrId)
                .createdAt(answer.getCreatedAt())
                .retrievedChunks(retrievedChunks)
                .sourceNodes(sourceNodes)
                .ragMeta(ragMeta)
                .build();
    }

    // RAG 사용하지 않는 응답 생성
    @Override
    public CreateChatbotResponseDto createSimpleAnswer(String graphStrId, CreateChatbotRequestDto createChatbotRequestDto) {
        Long graphId = Long.valueOf(graphStrId);

        // 404 : 지식그래프 찾을 수 없음
        Graph graph = graphRepository.getByGraph(graphId);

        // 새로운 대화인 경우 기존 채팅 삭제
        if (createChatbotRequestDto.isNewChat()) {
            deletePreviousChat(graphId);
        }

        // 기존 채팅 내역 조회
        List<Chatting> chatHistory = chattingRepository.findAllByGraphId(graphId);

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
