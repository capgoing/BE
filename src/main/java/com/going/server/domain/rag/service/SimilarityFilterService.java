package com.going.server.domain.rag.service;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimilarityFilterService {

    // 간단히 모든 문장을 통과시키는 기본 구현 (추후 유사도 필터링 적용 가능)
    public List<String> filterRelevantSentences(String userQuestion, List<String> sentences) {
        return sentences;
    }
}