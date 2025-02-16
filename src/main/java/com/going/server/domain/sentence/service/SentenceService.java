package com.going.server.domain.sentence.service;

import java.util.List;

public interface SentenceService {
    List<String> getSentence(Long wordId);
}
