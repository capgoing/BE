package com.going.server.domain.rag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphQueryResult {
    private String sourceLabel;
    private String relationLabel;
    private String targetLabel;
    private String sentence;
    private String nodeLabel;

    public String toTripleString() {
        return String.format("(%s)-[:RELATED {label: '%s'}]->(%s)", sourceLabel, relationLabel, targetLabel);
    }
}