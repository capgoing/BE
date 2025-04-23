package com.going.server.domain.graph.exception;

import com.going.server.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.going.server.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum GraphErrorCode implements BaseErrorCode {
    GRAPH_NOT_FOUND(NOT_FOUND, "GRAPH_404_1", "존재하지 않는 graph_id 입니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}