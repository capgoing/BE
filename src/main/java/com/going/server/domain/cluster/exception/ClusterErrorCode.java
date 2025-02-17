package com.going.server.domain.cluster.exception;

import com.going.server.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.going.server.global.constant.StaticValue.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ClusterErrorCode implements BaseErrorCode {
    CLUSTER_NOT_FOUND(NOT_FOUND, "CLUSTER_404_1","존재하지 않는 cluster_id입니다");

    private final int httpStatus;
    private final String code;
    private final String message;
}
