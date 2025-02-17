package com.going.server.domain.cluster.exception;

import com.going.server.global.exception.BaseException;

public class ClusterNotFoundException extends BaseException {
    public ClusterNotFoundException() {
        super(ClusterErrorCode.CLUSTER_NOT_FOUND);
    }
}
