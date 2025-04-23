package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.Graph;
import jdk.jfr.Registered;
import org.springframework.data.neo4j.repository.Neo4jRepository;

@Registered
public interface GraphRepository extends Neo4jRepository<Graph, Long> {
}
