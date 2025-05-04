package com.going.server.domain.quiz.service;

import com.going.server.domain.quiz.dto.QuizCreateResponseDto;

public interface QuizService {

    // 모드 별 퀴즈 생성
    public QuizCreateResponseDto quizCreate(String graphIdStr, String mode);

    // 만점일 경우 Graph Quiz 정보 업데이트
    void updateIfPerfect(String graphIdStr, String mode);
}
