package com.going.server.domain.quiz.exception;

import com.going.server.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.going.server.global.constant.StaticValue.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum QuizErrorCode implements BaseErrorCode {
    UNSUPPORTED_QUIZ_MODE(BAD_REQUEST, "QUIZ_400_1", "지원하지 않는 mode 입니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}