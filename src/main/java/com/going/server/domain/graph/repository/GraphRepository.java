package com.going.server.domain.graph.repository;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.GraphNotFoundException;
import jdk.jfr.Registered;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Registered
public interface GraphRepository extends Neo4jRepository<Graph, Long> {
    default Graph getByGraph(Long graphId) {
        return findById(graphId).orElseThrow(GraphNotFoundException::new);
    }
//    default Graph getByGraph(Long graphId) {
//        return findGraphWithEdgesByGraphId(graphId).orElseThrow(GraphNotFoundException::new);
//    }

//    @Query("""
//MATCH (g:Graph {id: $graphId})-[:HAS_NODE]->(n:GraphNode)
//OPTIONAL MATCH (n)-[r:RELATED]->(m:GraphNode)
//RETURN g, collect(DISTINCT n) as nodes, collect(DISTINCT r) as rels
//""")
//    Optional<Graph> findGraphWithEdgesByGraphId(@Param("graphId") Long graphId);


    @Query("MATCH (g:Graph {id: $graphId}) RETURN id(g)")
    Long findDbIdByGraphId(@Param("graphId") Long graphId);

    @Query("MATCH (g:Graph) RETURN max(g.id)")
    Long findMaxGraphId();
}
