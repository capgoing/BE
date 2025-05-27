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

    // RAG: 키워드 기반 노드 검색 (엣지 포함 x)
    @Query("""
        MATCH (g:Graph)-[:HAS_NODE]->(n:GraphNode)
        WHERE id(g) = $graphId AND
              ANY(kw IN $keywords WHERE
                  toLower(n.label) CONTAINS toLower(kw) OR
                  toLower(n.includeSentence) CONTAINS toLower(kw))
        RETURN n
    """)
    List<GraphNode> findByGraphIdAndKeywords(Long graphId, List<String> keywords);

    // RAG: 키워드 기반 노드 검색 (엣지 포함)
    @Query("""
        MATCH (g:Graph)-[:HAS_NODE]->(n:GraphNode)
        OPTIONAL MATCH (n)-[r:RELATED]->(m:GraphNode)
        WHERE id(g) = $graphId AND
              ANY(kw IN $keywords WHERE
                  toLower(n.label) CONTAINS toLower(kw) OR
                  toLower(n.includeSentence) CONTAINS toLower(kw))
        RETURN DISTINCT n, collect(r) AS edges
    """)
    List<GraphNode> findByGraphIdAndKeywordsWithEdges(Long graphId, List<String> keywords);
}
