package com.going.server.domain.rag.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
// 유사도 검사
public class SimilarityFilterServiceImpl implements SimilarityFilterService {
    // TODO : 정확한 문맥 유사도 필터로 개선 필요
    @Override
    public List<String> filterRelevantSentences(String query, List<String> candidates) {
        return candidates.stream()
                .filter(sentence -> sentence.toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
}
