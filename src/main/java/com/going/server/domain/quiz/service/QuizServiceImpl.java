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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService{
    private final GraphRepository graphRepository;
    private final ListenUpQuizGenerator listenUpQuizGenerator;
    private final ConnectQuizGenerator connectQuizGenerator;
    private final PictureQuizGenerator pictureQuizGenerator;

    // 모드 별 퀴즈 생성
    @Override
    public QuizCreateResponseDto quizCreate(String graphIdStr, String mode) {
        Long dbId = graphRepository.findDbIdByGraphId(Long.valueOf(graphIdStr));
        Graph graph = graphRepository.getByGraph(dbId);


        Object quizDto = switch (mode) {
            case "listenUp" -> listenUpQuizGenerator.generate(graph);
            case "connect" -> connectQuizGenerator.generate(graph);
            case "picture" -> pictureQuizGenerator.generate(graph);
            default -> throw new UnsupportedQuizModeException();
        };

        return new QuizCreateResponseDto<>(graphIdStr, mode, quizDto);
    }

    // 만점일 경우 Graph Quiz 정보 업데이트
    @Override
    public void updateIfPerfect(String graphIdStr, String mode) {
        Long dbId = graphRepository.findDbIdByGraphId(Long.valueOf(graphIdStr));
        Graph graph = graphRepository.getByGraph(dbId);

        switch (mode){
            case "listenUp":
                // 이미 해당 모드 퀴즈가 만점일 경우 -> 넘어가기
                if (graph.isListenUpPerfect()) {
                    return;
                }
                // listenUp 모드 만점 설정
                graph.setListenUpPerfect(true);
                break;
            case "connect":
                if (graph.isConnectPerfect()) {
                    return;
                }
                // connect 모드 만점 설정
                graph.setConnectPerfect(true);
                break;
            case "picture":
                if (graph.isPicturePerfect()) {
                    return;
                }
                // picture 모드 만점 설정
                graph.setPicturePerfect(true);
                break;
        }
        // DB에 저장
        graphRepository.save(graph);
    }
}