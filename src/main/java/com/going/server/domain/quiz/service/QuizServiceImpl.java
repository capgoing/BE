package com.going.server.domain.quiz.service;

import com.going.server.domain.quiz.dto.QuizCreateResponseDto;

public class QuizServiceImpl implements QuizService{


    @Override
    public QuizCreateResponseDto quizCreate(String mode, String graphId) {
        // 404 : 지식그래프 찾을 수 없음


        // TODO : mode별 분기 필요
        switch (mode) {

        }

        // TODO : graphId로 지식그래프 예시 문장 3개 랜덤으로 가져오기

        // TODO : 각 문장을 반환 형식에 맞게 가공
        return null;
    }
}
