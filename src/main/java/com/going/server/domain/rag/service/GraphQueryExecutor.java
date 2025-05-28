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

    private final Driver neo4jDriver; // Neo4j Java Driver

    public List<GraphQueryResult> runQuery(Long graphId, String cypherQuery) {
        List<GraphQueryResult> results = new ArrayList<>();

        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypherQuery);
            while (result.hasNext()) {
                Record record = result.next();

                // 필드 이름은 Cypher 쿼리 결과와 일치해야 함
                String sentence = record.get("sentence").asString("");
                String nodeLabel = record.get("nodeLabel").asString("");

                results.add(new GraphQueryResult(sentence, nodeLabel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}