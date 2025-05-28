package com.going.server.domain.rag.service;

import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.chatbot.repository.ChattingRepository;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.rag.util.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphRAGService {

    private final GraphRepository graphRepository;
    private final GraphNodeRepository graphNodeRepository;
    private final SimilarityFilterService similarityFilterService;
    private final PromptBuilder promptBuilder;
    private final ChattingRepository chattingRepository;
    private final CypherQueryGenerator cypherQueryGenerator;
    private final GraphQueryExecutor graphQueryExecutor;
    private final RagAnswerCreateService ragAnswerCreateService;


    // GraphRAG 응답 생성
    public CreateChatbotResponseDto createAnswerWithGraphRAG(
            Long graphId,
            String userQuestion,
            List<Chatting> chatHistory
    ){
        Graph graph = graphRepository.getByGraph(graphId);

        // 1. 질문 → Cypher 쿼리 생성 (LLM)
        String cypherQuery = cypherQueryGenerator.generate(userQuestion);

        // 2. 쿼리 실행 → 결과 추출
        List<String> contextChunks = graphQueryExecutor.runQuery(graphId, cypherQuery);

        // 3. 프롬프트 구성
        String finalPrompt = promptBuilder.buildPrompt(contextChunks, userQuestion);

        // 4. 응답 생성
        String response = contextChunks.isEmpty()
                ? ragAnswerCreateService.chat(chatHistory, userQuestion)
                : ragAnswerCreateService.chatWithContext(chatHistory, finalPrompt);

        Chatting answer = Chatting.ofGPT(graph, response);
        chattingRepository.save(answer);

        return CreateChatbotResponseDto.of(
                response,
                graphId.toString(),
                answer.getCreatedAt(),
                contextChunks,
                null // sourceNodes: 필요하면 쿼리 결과에서 추출
        );
    }
}
