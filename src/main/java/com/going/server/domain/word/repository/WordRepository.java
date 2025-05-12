package com.going.server.domain.word.repository;

import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.exception.WordNotFoundException;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends Neo4jRepository<Word, Long> {
    Optional<Word> findById(Long wordId);

    List<Word> findByCluster_ClusterId(Long clusterId);

    default Word getByWord(Long wordId) {
        return findById(wordId).orElseThrow(WordNotFoundException::new);
    }
}
