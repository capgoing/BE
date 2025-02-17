package com.going.server.domain.cluster.repository;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.exception.ClusterNotFoundException;
import com.going.server.domain.word.exception.WordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    Cluster findByRepresentWord(String word);

    default Cluster getByCluster(Long ClusterId) {
        return findById(ClusterId).orElseThrow(ClusterNotFoundException::new);
    }
}
