package com.going.server.domain.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// 퀴즈 생성 반환 DTO
public class QuizCreateResponseDto {
    private String graphId;
    private String mode; // listenUp, connect, picture

    @Schema(oneOf = {
            ListenUpQuizDto.class,
            ConnectQuizDto.class,
            PictureQuizDto.class})
    private Object quizzes;
}
