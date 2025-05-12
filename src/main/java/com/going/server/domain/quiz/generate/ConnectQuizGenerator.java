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
        Set<String> usedNodeIds = new HashSet<>();

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
    private static void createConnectQuiz(Random random, List<NodeDto> nodeDtoList,
                                          List<ConnectQuizDto.ConnectQuiz> quizList, Set<String> usedNodeIds) {
        if (usedNodeIds.size() >= nodeDtoList.size()) return;

        NodeDto targetNode;
        String targetId;

        // node의 ID(String) 기준 중복 체크
        do {
            int randomIndex = random.nextInt(nodeDtoList.size());
            targetNode = nodeDtoList.get(randomIndex);
            targetId = String.valueOf(targetNode.getId());
        } while (usedNodeIds.contains(targetId));

        usedNodeIds.add(targetId); // 사용된 ID 등록

        String answer = targetNode.getLabel();

        Set<String> options = new HashSet<>();
        options.add(answer);

        while (options.size() < 5) {
            int randomIndex = random.nextInt(nodeDtoList.size());
            String option = nodeDtoList.get(randomIndex).getLabel();
            options.add(option);
        }

        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);

        ConnectQuizDto.ConnectQuiz quiz = ConnectQuizDto.ConnectQuiz.builder()
                .questionTargetId(targetId) // ID는 이제 문자열
                .shuffledOptions(shuffledOptions)
                .answer(answer)
                .build();

        quizList.add(quiz);
    }
}
