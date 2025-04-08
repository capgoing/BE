package com.going.server.domain.cluster.repository;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.exception.ClusterNotFoundException;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClusterRepository extends Neo4jRepository<Cluster, Long> {
    Cluster findByRepresentWord(String word);

    Optional<Cluster> findByClusterId(Long clusterId);

    default Cluster getByCluster(Long clusterId) {
        return findByClusterId(clusterId).orElseThrow(ClusterNotFoundException::new);
    }
}
