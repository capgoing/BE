package com.going.server.domain.rag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphQueryResult {

    private final String sentence;   // RAG에 활용할 문장
    private final String nodeLabel;  // 해당 문장이 포함된 노드의 라벨 또는 ID

}