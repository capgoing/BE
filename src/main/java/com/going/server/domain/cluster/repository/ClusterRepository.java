package com.going.server.domain.cluster.repository;

import com.going.server.domain.cluster.entity.Cluster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterRepository extends CrudRepository<Cluster, Long> {
}
