package com.going.server.domain.history.repository;

import com.going.server.domain.history.entity.History;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends Neo4jRepository<History, Long> {
}
