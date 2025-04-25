package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.GraphEdge;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GraphEdgeRepository extends Neo4jRepository<GraphEdge, Long> {
}
