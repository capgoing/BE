package com.going.server.domain.word.repository;

import com.going.server.domain.word.entity.Word;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordRepository extends MongoRepository<Word,Long> {
}
