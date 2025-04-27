package com.going.server.domain.quiz.exception;

import com.going.server.global.exception.BaseException;

public class QuizImageException extends BaseException {
    public QuizImageException(){super(QuizErrorCode.QUIZ_IMAGE_GENERATION_FAILED);}
}
