package com.going.server.domain.quiz.service;

import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface QuizService {

    // 모드 별 퀴즈 생성
    public QuizCreateResponseDto quizCreate(String mode, String graphId);
}
