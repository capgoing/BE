package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.NodeNotFoundException;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface GraphNodeRepository extends Neo4jRepository<GraphNode, Long> {
    Optional<GraphNode> findByNodeId(Long nodeId);
    default GraphNode getByNode(Long nodeId) {
        return findByNodeId(nodeId).orElseThrow(NodeNotFoundException::new);
    }

    @Query("MATCH (n:GraphNode) RETURN COALESCE(MAX(n.node_id), 0)")
    Long findMaxNodeId();

}
