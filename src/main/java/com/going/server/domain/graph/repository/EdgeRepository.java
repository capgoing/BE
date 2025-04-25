package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.Edge;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EdgeRepository extends Neo4jRepository<Edge, Long> {
}
