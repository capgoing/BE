package com.going.server.domain.sentence.repository;

import com.going.server.domain.sentence.entity.Sentence;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentenceRepository extends Neo4jRepository<Sentence, Long> {
    List<Sentence> findByWord_Id(Long wordId);
}
