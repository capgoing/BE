package com.going.server.domain.quiz.service;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.exception.GraphNotFoundException;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.quiz.dto.ConnectQuizDto;
import com.going.server.domain.quiz.dto.ListenUpQuizDto;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QuizServiceImpl implements QuizService{
    private final GraphRepository graphRepository;

    @Override
    public QuizCreateResponseDto quizCreate(String mode, String graphIdStr) {
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
        // TODO : listenUp 퀴즈 생성 로직 작성
        return new ListenUpQuizDto();
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