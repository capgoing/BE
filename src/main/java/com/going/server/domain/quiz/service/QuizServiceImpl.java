package com.going.server.domain.quiz.service;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@AllArgsConstructor
public class QuizServiceImpl implements QuizService{
    private final GraphRepository graphRepository;



    @Override
    public QuizCreateResponseDto quizCreate(String mode, String graphIdStr) {
        Long graphId = Long.valueOf(graphIdStr);

        Optional<Graph> foundGraph = graphRepository.findById(graphId);

        // 404 : 지식그래프 찾을 수 없음

        // TODO : mode별 분기 필요
        switch (mode) {
            case "listenUp":
                listenUpQuizCreate();

                break;


        }

        // TODO : graphId로 지식그래프 예시 문장 3개 랜덤으로 가져오기

        // TODO : 각 문장을 반환 형식에 맞게 가공
        return null;
    }
}
