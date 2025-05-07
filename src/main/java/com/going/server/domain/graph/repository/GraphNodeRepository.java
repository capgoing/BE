package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.NodeNotFoundException;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface GraphNodeRepository extends Neo4jRepository<GraphNode, Long> {
    Optional<GraphNode> findByNodeId(Long nodeId);
    default GraphNode getByNode(Long nodeId) {
        return findByNodeId(nodeId).orElseThrow(NodeNotFoundException::new);
    }

    @Query("MATCH (n:GraphNode) RETURN COALESCE(MAX(n.node_id), 0)")
    Long findMaxNodeId();

    // RAG: 키워드 기반 노드 검색
    @Query("""
        MATCH (n:GraphNode)
        WHERE toLower(n.includeSentence) CONTAINS toLower($keyword)
           OR toLower(n.label) CONTAINS toLower($keyword)
        RETURN n
    ₩""")
    List<GraphNode> findByKeyword(String keyword);


}
