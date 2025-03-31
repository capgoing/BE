package com.going.server.domain.cluster.repository;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.exception.ClusterNotFoundException;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterRepository extends Neo4jRepository<Cluster, Long> {
    Cluster findByRepresentWord(String word);

    default Cluster getByCluster(Long clusterId) {
        return findById(clusterId).orElseThrow(ClusterNotFoundException::new);
    }
}
