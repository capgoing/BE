package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.NodeNotFoundException;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GraphNodeRepository extends Neo4jRepository<GraphNode, Long> {
    default GraphNode geyByNode(Long nodeId) {
        return findById(nodeId).orElseThrow(NodeNotFoundException::new);
    }
}
