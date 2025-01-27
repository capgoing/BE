package com.going.server.global.response;

import com.sun.net.httpserver.Authenticator.Success;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SuccessResponse<T> extends BaseResponse {

    private final T data;

    public SuccessResponse(T data) {
        super(true, "200", "호출에 성공하였습니다.");
        this.data = data;
    }

    public SuccessResponse(T data, String code) {
        super(true, code, "호출에 성공하였습니다.");
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data);
    }

    public static <T> SuccessResponse<T> of(T data, String code) {
        return new SuccessResponse<>(data, code);
    }

    public static <T> SuccessResponse<T> empty() {
        return new SuccessResponse<>(null);
    }
}
