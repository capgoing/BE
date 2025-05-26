package com.going.server.domain.graph.exception;

import com.going.server.global.exception.BaseException;

public class GraphContentNotFoundException extends BaseException {
    public GraphContentNotFoundException() {
        super(GraphErrorCode.GRAPH_CONTENT_EMPTY);
    }
}
