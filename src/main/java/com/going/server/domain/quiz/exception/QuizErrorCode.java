package com.going.server.domain.quiz.exception;

import com.going.server.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.going.server.global.constant.StaticValue.BAD_REQUEST;
import static com.going.server.global.constant.StaticValue.INTERNAL_SERVER_ERROR;

@Getter
@AllArgsConstructor
public enum QuizErrorCode implements BaseErrorCode {
    // 400
    UNSUPPORTED_QUIZ_MODE(BAD_REQUEST, "QUIZ_400_1", "지원하지 않는 mode 입니다."),

    // 500
    QUIZ_IMAGE_GENERATION_FAILED(INTERNAL_SERVER_ERROR, "QUIZ_500_1", "퀴즈 이미지 생성 중 서버 오류가 발생했습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}