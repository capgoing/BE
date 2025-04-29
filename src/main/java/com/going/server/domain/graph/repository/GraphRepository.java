package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.exception.GraphNotFoundException;
import jdk.jfr.Registered;
import org.springframework.data.neo4j.repository.Neo4jRepository;

@Registered
public interface GraphRepository extends Neo4jRepository<Graph, Long> {
    default Graph getByGraph(Long graphId) {
        return findById(graphId).orElseThrow(GraphNotFoundException::new);
    }

}
