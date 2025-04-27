package com.going.server.domain.quiz.exception;

import com.going.server.global.exception.BaseException;

public class UnsupportedQuizModeException extends BaseException {
    public UnsupportedQuizModeException() {super(QuizErrorCode.UNSUPPORTED_QUIZ_MODE);}
}
