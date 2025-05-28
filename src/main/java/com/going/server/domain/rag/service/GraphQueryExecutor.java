package com.going.server.domain.rag.service;

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

    public List<String> runQuery(Long graphId, String cypherQuery) {
        List<String> results = new ArrayList<>();

        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypherQuery);
            while (result.hasNext()) {
                Record record = result.next();
                results.add(record.toString()); // 필요에 따라 특정 필드만 추출 가능
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}