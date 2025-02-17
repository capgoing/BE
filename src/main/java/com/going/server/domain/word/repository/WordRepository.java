package com.going.server.domain.word.repository;

import com.going.server.domain.word.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByCluster_ClusterId(Long clusterId);
}
