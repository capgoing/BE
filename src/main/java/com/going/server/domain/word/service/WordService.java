package com.going.server.domain.word.service;

import com.going.server.domain.word.dto.WordResponseDto;

public interface WordService {
    WordResponseDto getWordList(Long clusterId);

    void deleteWord(Long wordId);
}
