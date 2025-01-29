package com.going.server.domain.word.service;

import com.going.server.domain.word.dto.WordResponseDto;

import java.util.List;

public interface WordService {
    List<WordResponseDto> getWords();

    List<WordResponseDto> getCompositionWords();
}
