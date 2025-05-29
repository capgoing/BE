package com.going.server.domain.rag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphQueryResult {
    private String sentence;         // 예: "물은 응고되어 얼음이 된다."
    private String sourceLabel;      // 예: "물"
    private String relationLabel;    // 예: "응고"
    private String targetLabel;      // 예: "얼음"
    private String nodeLabel;        // 예: "물" (질의어에 가까운 노드)

    public String toTripleString() {
        if (sourceLabel == null || relationLabel == null || targetLabel == null) return null;

        // 혹시 내부 문자열이 "null"로 들어오는 것도 막기
        if ("null".equals(sourceLabel) || "null".equals(relationLabel) || "null".equals(targetLabel)) return null;

        return String.format("(%s)-[:RELATED {label: '%s'}]->(%s)", sourceLabel, relationLabel, targetLabel);
    }

}
