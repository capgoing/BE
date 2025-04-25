package com.going.server.domain.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 퀴즈 생성 반환 DTO
@AllArgsConstructor
@Getter
public class QuizCreateResponseDto<T> {
    private String graphId;
    private String mode; // listenUp, connect, picture

    @Schema(oneOf = {
            ListenUpQuizDto.class,
            ConnectQuizDto.class,
            PictureQuizDto.class})
    private T quizzes;
}
