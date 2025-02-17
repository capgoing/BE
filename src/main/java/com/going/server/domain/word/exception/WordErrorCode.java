package com.going.server.domain.word.exception;

import com.going.server.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.going.server.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum WordErrorCode implements BaseErrorCode {
    WORD_NOT_FOUND(NOT_FOUND,"WORD_404_1","존재하지 않는 word_id입니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}
