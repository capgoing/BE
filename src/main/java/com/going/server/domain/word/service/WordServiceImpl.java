package com.going.server.domain.word.service;

import com.going.server.domain.word.dto.WordResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    @Override
    public List<WordResponseDto> getWords() {
        return List.of();
    }

    @Override
    public List<WordResponseDto> getCompositionWords() {
        return List.of();
    }
}
