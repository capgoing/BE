package com.going.server.domain.rag.service;

import java.util.List;

public interface SimilarityFilterService {
    List<String> filterRelevantSentences(String question, List<String> candidateSentences);
}
