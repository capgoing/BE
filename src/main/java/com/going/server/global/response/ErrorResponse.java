package com.going.server.global.response;

import com.going.server.global.exception.BaseErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse extends BaseResponse {

    private final int httpStatus;

    @Builder
    public ErrorResponse(String code, String message, int httpStatus) {
        super(false, code, message);
        this.httpStatus = httpStatus;
    }

    public static ErrorResponse of(BaseErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .httpStatus(errorCode.getHttpStatus())
                .build();
    }

    public static ErrorResponse of(BaseErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .httpStatus(errorCode.getHttpStatus())
                .build();
    }

    public static ErrorResponse of(String code, String message, int httpStatus) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
