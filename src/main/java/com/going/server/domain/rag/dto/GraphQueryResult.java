package com.going.server.domain.rag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphQueryResult {
    private String sentence;
    private String nodeLabel;
}