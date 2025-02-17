package com.going.server.domain.word.exception;

import com.going.server.global.exception.BaseException;
import org.apache.coyote.BadRequestException;

public class WordNotFoundException extends BaseException {
    public WordNotFoundException() {
        super(WordErrorCode.WORD_NOT_FOUND);
    }
}
