package com.going.server.domain.rag.service;

import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.repository.ChattingRepository;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.rag.util.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraphRAGService {

    private final GraphRepository graphRepository;
    private final GraphNodeRepository graphNodeRepository;
    private final SimilarityFilterService similarityFilterService;
    private final PromptBuilder promptBuilder;
    private final ChattingRepository chattingRepository;
    private final KeywordExtractor keywordExtractor;
    private final RagAnswerCreateService ragAnswerCreateService;

    public CreateChatbotResponseDto createAnswerWithRAG(Long graphId, String userQuestion, boolean isNewChat) {
        Graph graph = graphRepository.getByGraph(graphId);

        // 키워드 추출
        List<String> keywords = keywordExtractor.extract(userQuestion);

        // 관련 노드 및 문장
        List<GraphNode> matchedNodes = graphNodeRepository.findByGraphIdAndKeywords(graphId, keywords);
        List<String> candidateSentences = matchedNodes.stream()
                .map(GraphNode::getIncludeSentence)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 필터링
        List<String> filteredChunks = similarityFilterService.filterRelevantSentences(userQuestion, candidateSentences);
        String finalPrompt = promptBuilder.buildPrompt(filteredChunks, userQuestion);

        // 대화 내역
        if (isNewChat) chattingRepository.deleteAllByGraphId(graphId);
        List<Chatting> chatHistory = chattingRepository.findAllByGraphId(graphId);

        // 질문 저장
        chattingRepository.save(Chatting.ofUser(graph, userQuestion));

        // 응답 생성
        String response = filteredChunks.isEmpty()
                ? ragAnswerCreateService.chat(chatHistory, userQuestion)
                : ragAnswerCreateService.chatWithContext(chatHistory, finalPrompt);

        // 응답 저장
        Chatting answer = Chatting.ofGPT(graph, response);
        chattingRepository.save(answer);

        return CreateChatbotResponseDto.of(
                response,
                graphId.toString(),
                answer.getCreatedAt(),
                filteredChunks,
                matchedNodes.stream().map(GraphNode::getLabel).distinct().toList()
        );
    }
}
