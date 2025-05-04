package com.going.server.domain.chatbot.repository;

import com.going.server.domain.chatbot.entity.Chatting;
import com.going.server.domain.graph.entity.Graph;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRepository extends Neo4jRepository<Chatting, Long> {
    @Query("""
    MATCH (c:Chatting)-[:BELONGS_TO]->(g:Graph)
    WHERE id(g) = $graphId
    DETACH DELETE c
    """)
    void deleteByGraphId(Long graphId);

    // GraphId로 모든 Chatting 조회
    @Query("""
    MATCH (c:Chatting)-[:BELONGS_TO]->(g:Graph)
    WHERE id(g) = $graphId
    RETURN c ORDER BY c.createdAt
    """)
    List<Chatting> findAllByGraphId(@Param("graphId") Long graphId);
}
