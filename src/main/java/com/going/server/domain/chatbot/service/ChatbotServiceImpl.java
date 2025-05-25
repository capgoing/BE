package com.going.server.domain.chatbot.service;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.entity.Sender;
import com.going.server.domain.chatbot.repository.ChattingRepository;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.openai.service.RAGAnswerCreateService;
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
    private final RAGAnswerCreateService ragAnswerCreateService;

    // 원문 반환
    @Override
    public String getOriginalText(String graphId) {
        Graph graph = graphRepository.getByGraph(Long.valueOf(graphId));
        return graph.getContent();
    }

    // 요약본 생성
    @Override
    public String getSummaryText(String graphId) {
        Graph graph = graphRepository.getByGraph(Long.valueOf(graphId));
        String context = graph.getContent();  // 지식그래프 본문 내용
        return textSummaryCreateService.summarize(context);  // 요약본 생성
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
    public CreateChatbotResponseDto createSimpleAnswer(String graphId, CreateChatbotRequestDto dto) {
        return null;
    }

    // 4컷만화 생성
    @Override
    public CreateChatbotResponseDto createCartoon(String graphId, CreateChatbotRequestDto dto) {
        return null;
    }

    // 추천 영상 생성
    @Override
    public CreateChatbotResponseDto recommendVideo(String graphId, CreateChatbotRequestDto dto) {
        return null;
    }

    // 채팅 삭제 메서드
    private void deletePreviousChat(Long graphId) {
        chattingRepository.deleteByGraphId(graphId);
    }
}
