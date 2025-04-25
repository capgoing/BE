package com.going.server.domain.quiz.service;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.GraphNotFoundException;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.quiz.dto.ConnectQuizDto;
import com.going.server.domain.quiz.dto.ListenUpQuizDto;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService{
    private final GraphRepository graphRepository;

    @Override
    public QuizCreateResponseDto quizCreate(String graphIdStr, String mode) {
        Long graphId = Long.valueOf(graphIdStr);

        // 404 : 지식그래프 찾을 수 없음
        Graph graph = graphRepository.findById(graphId)
                .orElseThrow(GraphNotFoundException::new);

        Object quizDto = null;

        switch (mode) {
            case "listenUp":
                quizDto = listenUpQuizCreate(graph);
                break;
            case "connect":
                quizDto = connectQuizCreate(graph);
                break;
            case "picture":
                quizDto = pictureQuizCreate(graph);
                break;
            default:
                // TODO : 퀴즈 모드 관련 예외처리 필요
        }

        return new QuizCreateResponseDto<>(graphIdStr, mode, quizDto);
    }

    // listenUp 퀴즈 생성 메서드
    private ListenUpQuizDto listenUpQuizCreate(Graph graph) {
        // 최종 리턴할 퀴즈 리스트 (3개)
        List<ListenUpQuizDto.ListenUpQuiz> quizzes = new ArrayList<>();

        Random random = new Random();

        // 몇 개 퀴즈 만들었는지 세기 위한 변수 (3개까지만 만들 거임)
        int count = 0;

        // 그래프에 연결된 노드를 하나씩 확인
        for (GraphNode node : graph.getNodes()) {

            // includeSentence가 null이거나 공백이면 건너뛰기
            if (node.getIncludeSentence() == null || node.getIncludeSentence().isBlank()) {
                continue;
            }

            // includeSentence 문장을 띄어쓰기 기준으로 단어 분리
            String[] words = node.getIncludeSentence().trim().split("\\s+");

            // 단어 리스트를 정답 리스트로 저장
            List<String> answer = Arrays.asList(words);

            // 정답 리스트를 복사해서 셔플할 리스트로 사용
            List<String> shuffled = new ArrayList<>(answer);
            Collections.shuffle(shuffled, random); // 무작위로 단어 순서 섞기

            // 하나의 퀴즈 생성
            ListenUpQuizDto.ListenUpQuiz quiz = ListenUpQuizDto.ListenUpQuiz.builder()
                    .answer(answer)               // 정답 리스트
                    .shuffled(shuffled)           // 섞인(문제) 리스트
                    .description(node.getIncludeSentence()) // TTS로 읽어줄 원문 문장
                    .build();

            // 퀴즈 리스트에 추가
            quizzes.add(quiz);
            count++;

            // 3개 만들었으면 종료
            if (count >= 3) break;
        }

        // 최종 퀴즈 DTO에 담아서 리턴
        return ListenUpQuizDto.builder()
                .quizzes(quizzes)
                .build();
    }

    // connect 퀴즈 생성 메서드
    private ConnectQuizDto connectQuizCreate(Graph graph) {
        // TODO : connect 퀴즈 생성 로직 작성
        return new ConnectQuizDto();
    }

    // picture 퀴즈 생성 메서드
    private PictureQuizDto pictureQuizCreate(Graph graph) {
        // TODO : picture 퀴즈 생성 로직 작성
        return new PictureQuizDto();
    }
}