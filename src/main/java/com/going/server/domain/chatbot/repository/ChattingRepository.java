package com.going.server.domain.chatbot.repository;

import com.going.server.domain.chatbot.entity.Chatting;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRepository extends Neo4jRepository<Chatting, Long> {
}
