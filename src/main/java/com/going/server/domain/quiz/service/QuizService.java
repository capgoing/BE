package com.going.server.domain.quiz.service;

import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface QuizService {

    // ListenUp 퀴즈 생성
    public QuizCreateResponseDto QuizCreate(String mode, String graphId);
}
