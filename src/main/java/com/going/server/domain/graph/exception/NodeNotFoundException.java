package com.going.server.domain.graph.exception;

import com.going.server.global.exception.BaseException;

public class NodeNotFoundException extends BaseException {
    public NodeNotFoundException() {
        super(NodeErrorCode.NODE_NOT_FOUND);
    }
}
