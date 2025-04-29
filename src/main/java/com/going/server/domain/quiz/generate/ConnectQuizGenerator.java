package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.dto.EdgeDto;
import com.going.server.domain.graph.dto.KnowledgeGraphDto;
import com.going.server.domain.graph.dto.NodeDto;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.quiz.dto.ConnectQuizDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConnectQuizGenerator implements QuizGenerator<ConnectQuizDto> {

    @Override
    public ConnectQuizDto generate(Graph graph) {

        // 1. 지식그래프 조회
        List<NodeDto> nodeDtoList = new ArrayList<>();
        List<EdgeDto> edgeDtoList = new ArrayList<>();

        for (GraphNode node : graph.getNodes()) {
            NodeDto nodeDto = NodeDto.from(node);
            nodeDtoList.add(nodeDto);

            if (node.getEdges() != null) {
                for (GraphEdge edge : node.getEdges()) {
                    EdgeDto edgeDto = EdgeDto.from(edge.getSource(),edge.getTarget().getNodeId().toString(),edge.getLabel());
                    edgeDtoList.add(edgeDto);
                }
            }
        }

        // 2. 문제 생성
        Random random = new Random();
        // 최종 문제 리스트
        List<ConnectQuizDto.ConnectQuiz> quizList = new ArrayList<>();
        // 이미 사용한 노드 Id 기록용 (중복 방지)
        Set<Integer> usedNodeIds = new HashSet<>();

        // 문제 3개 만들기
        for (int i = 0; i < 3; i++) {
            createConnectQuiz(random, nodeDtoList, quizList, usedNodeIds);
        }

        // 3. 반환
        return ConnectQuizDto.builder()
                .knowledgeGraph(KnowledgeGraphDto.of(nodeDtoList, edgeDtoList))
                .quizList(quizList)
                .build();
    }

    // connect 퀴즈 문제 생성
    private static void createConnectQuiz(Random random, List<NodeDto> nodeDtoList, List<ConnectQuizDto.ConnectQuiz> quizList, Set<Integer> usedNodeIndices) {
        if(usedNodeIndices.size() >= nodeDtoList.size()) {
            // 모든 노드를 다 사용했으면 추가 생성 불가
            return;
        }

        int questionTargetId;

        // nodeDtoList 중 1개의 id로 랜덤 선택 (중복 방지)
        do {
            questionTargetId = random.nextInt(nodeDtoList.size());
        } while (usedNodeIndices.contains(questionTargetId));

        NodeDto targetNode = nodeDtoList.get(questionTargetId);
        usedNodeIndices.add(questionTargetId); // 사용한 Id 추가

        // 정답
        String answer = targetNode.getLabel();

        // 정답 포함 5개 보기 생성
        Set<String> options = new HashSet<>();
        options.add(answer); // 정답 보기 추가

        while (options.size() < 5) { // 랜덤 보기 추가
            int randomIndex = random.nextInt(nodeDtoList.size());
            String option = nodeDtoList.get(randomIndex).getLabel();
            options.add(option);
        }

        // 보기 리스트 랜덤 배치
        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);

        // 문제 하나 생성
        ConnectQuizDto.ConnectQuiz quiz = ConnectQuizDto.ConnectQuiz.builder()
                .questionTargetId(String.valueOf(questionTargetId))
                .shuffledOptions(shuffledOptions)
                .answer(answer)
                .build();

        // 문제 리스트에 추가
        quizList.add(quiz);
    }
}
