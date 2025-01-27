package com.going.server.global.exception;

public interface BaseErrorCode {

    String getCode();
    String getMessage();
    int getHttpStatus();
}
