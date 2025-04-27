package com.going.server.domain.quiz.service;

import com.going.server.domain.graph.dto.EdgeDto;
import com.going.server.domain.graph.dto.KnowledgeGraphDto;
import com.going.server.domain.graph.dto.NodeDto;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.quiz.dto.ConnectQuizDto;
import com.going.server.domain.quiz.dto.ListenUpQuizDto;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import com.going.server.domain.quiz.generate.ConnectQuizGenerator;
import com.going.server.domain.quiz.generate.ListenUpQuizGenerator;
import com.going.server.domain.quiz.generate.PictureQuizGenerator;
import com.going.server.domain.quiz.generate.QuizGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService{
    private final GraphRepository graphRepository;
    private final ListenUpQuizGenerator listenUpQuizGenerator;
    private final ConnectQuizGenerator connectQuizGenerator;
    private final PictureQuizGenerator pictureQuizGenerator;

    @Override
    public QuizCreateResponseDto quizCreate(String graphIdStr, String mode) {
        Long graphId = Long.valueOf(graphIdStr);

        // 404 : 지식그래프 찾을 수 없음
        Graph graph = graphRepository.getByGraph(graphId);

        Object quizDto = switch (mode) {
            case "listenUp" -> listenUpQuizGenerator.generate(graph);
            case "connect" -> connectQuizGenerator.generate(graph);
            case "picture" -> pictureQuizGenerator.generate(graph);
            default -> throw new IllegalArgumentException("지원하지 않는 모드입니다: " + mode);
        };

        return new QuizCreateResponseDto<>(graphIdStr, mode, quizDto);
    }

    // listenUpQuizDto 생성 메서드
    private ListenUpQuizDto listenUpQuizCreate(Graph graph) {
        Random random = new Random();
        List<ListenUpQuizDto.ListenUpQuiz> quizzes = new ArrayList<>();
        Set<String> usedSentences = new HashSet<>();
        List<String> options = new ArrayList<>();

        // 1. 그래프 노드에서 문장 추출
        for (GraphNode node : graph.getNodes()) {
            if (node.getIncludeSentence() == null || node.getIncludeSentence().isBlank()) continue;

            // "." 으로 문장 나누기
            String[] splitSentences = node.getIncludeSentence().split("\\.");

            for (String rawSentence : splitSentences) {
                String sentence = rawSentence.trim();
                if (sentence.isBlank()) continue; // 공백은 스킵
                if (usedSentences.contains(sentence)) continue;

                String[] words = sentence.split("\\s+");
                if (words.length < 5) continue; // 5단어 미만은 스킵

                options.add(sentence);
            }
        }

        // 2. 단어 수 기준 정렬 (5단어에 가까운 순서)
        options.sort(Comparator.comparingInt(
                s -> Math.abs(s.trim().split("\\s+").length - 5)
        ));

        int count = 0;

        for (String sentence : options) {
            if (count >= 3) break;

            String[] words = sentence.split("\\s+");

            List<String> answer = new ArrayList<>();

            if (words.length == 5) { // 5단어면 그대로
                answer = Arrays.asList(words);
            } else {
                // 6단어 이상이면 랜덤하게 5개로 압축
                int mergeCount = words.length - 5; // 합쳐야 할 횟수
                List<String> wordList = new ArrayList<>(Arrays.asList(words));

                for (int i = 0; i < mergeCount; i++) {
                    int mergeIdx = random.nextInt(wordList.size() - 1); // 마지막 단어는 제외
                    String merged = wordList.get(mergeIdx) + " " + wordList.get(mergeIdx + 1);
                    wordList.set(mergeIdx, merged);
                    wordList.remove(mergeIdx + 1);
                }
                answer = wordList;
            }

            if (answer.size() != 5) continue; // 안전망

            List<String> shuffled = new ArrayList<>(answer);
            Collections.shuffle(shuffled, random);

            // 퀴즈 생성
            ListenUpQuizDto.ListenUpQuiz quiz = ListenUpQuizDto.ListenUpQuiz.builder()
                    .answer(answer)
                    .shuffled(shuffled)
                    .description(sentence) // 이 문장 전체가 TTS로 읽힐 문장
                    .build();

            quizzes.add(quiz);
            usedSentences.add(sentence);
            count++;
        }

        // 최종 퀴즈 DTO에 담아서 번환
        return ListenUpQuizDto.builder()
                .quizzes(quizzes)
                .build();
    }

    // ConnectQuizDto 생성 메서드
    private ConnectQuizDto createConnectQuizDto(Graph graph) {

        // 1. 지식그래프 조회
        List<NodeDto> nodeDtoList = new ArrayList<>();
        List<EdgeDto> edgeDtoList = new ArrayList<>();

        for (GraphNode node : graph.getNodes()) {
            NodeDto nodeDto = NodeDto.from(node, null);
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

        // 문제 3개 만들기
        for (int i = 0; i < 3; i++) {
            createConnectQuiz(random, nodeDtoList, quizList);
        }

        // 3. 반환
        return ConnectQuizDto.builder()
                .knowledgeGraph(KnowledgeGraphDto.of(nodeDtoList, edgeDtoList))
                .quizList(quizList)
                .build();
    }

    // picture 퀴즈 생성 메서드
    private PictureQuizDto pictureQuizCreate(Graph graph) {
        // TODO : picture 퀴즈 생성 로직 작성
        return PictureQuizDto.builder()
                .build();
    }

    // connect 퀴즈 문제 생성
    private static void createConnectQuiz(Random random, List<NodeDto> nodeDtoList, List<ConnectQuizDto.ConnectQuiz> quizList) {
        // nodeDtoList 중 1개의 id로 랜덤 선택
        int questionTargetId = random.nextInt(nodeDtoList.size());
        NodeDto targetNode = nodeDtoList.get(questionTargetId);

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