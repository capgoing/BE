package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.GraphNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GraphNodeRepository extends Neo4jRepository<GraphNode, Long> {
}
