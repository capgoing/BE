package com.going.server.domain.rag.service;

import com.going.server.domain.rag.dto.GraphQueryResult;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// 2. 쿼리 실행 → 결과 추출
@Component
@RequiredArgsConstructor
public class GraphQueryExecutor {

    private final Driver neo4jDriver;

    public List<GraphQueryResult> runQuery(Long graphId, String cypherQuery) {
        List<GraphQueryResult> results = new ArrayList<>();

        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypherQuery);

            while (result.hasNext()) {
                Record record = result.next();

                String sentence = getSafeString(record, "sentence");
                String nodeLabel = getSafeString(record, "nodeLabel");

                String sourceLabel = getSafeString(record, "sourceLabel");
                String relationLabel = getSafeString(record, "relationLabel");
                String targetLabel = getSafeString(record, "targetLabel");

                results.add(new GraphQueryResult(
                        sentence,
                        nodeLabel,
                        sourceLabel,
                        relationLabel,
                        targetLabel
                ));
            }

        } catch (Exception e) {
            System.err.println("[GraphRAG] Cypher 쿼리 실행 중 오류 발생:");
            e.printStackTrace();
        }

        return results;
    }

    // 안전한 String 추출 (null-safe)
    private String getSafeString(Record record, String key) {
        return record.containsKey(key) && !record.get(key).isNull()
                ? record.get(key).asString()
                : null;
    }
}