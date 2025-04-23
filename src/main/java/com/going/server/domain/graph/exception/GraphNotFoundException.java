package com.going.server.domain.graph.exception;

import com.going.server.global.exception.BaseException;

public class GraphNotFoundException extends BaseException {
    public GraphNotFoundException() {
        super(GraphErrorCode.GRAPH_NOT_FOUND);
    }
}
