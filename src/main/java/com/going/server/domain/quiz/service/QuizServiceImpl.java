package com.going.server.domain.quiz.service;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import com.going.server.domain.quiz.exception.UnsupportedQuizModeException;
import com.going.server.domain.quiz.generate.ConnectQuizGenerator;
import com.going.server.domain.quiz.generate.ListenUpQuizGenerator;
import com.going.server.domain.quiz.generate.PictureQuizGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
            default -> throw new UnsupportedQuizModeException();
        };

        return new QuizCreateResponseDto<>(graphIdStr, mode, quizDto);
    }
}