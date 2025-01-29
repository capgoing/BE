package com.going.server.domain.word.controller;

import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.service.WordService;
import com.going.server.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    //대표어휘 조회
    @GetMapping()
    public SuccessResponse<List<WordResponseDto>> getWords() {
        List<WordResponseDto> response = wordService.getWords();
        return SuccessResponse.of(response);
    }

    //구성어휘 조회
    @GetMapping("/composition-word")
    public SuccessResponse<List<WordResponseDto>> getCompositionWords() {
        List<WordResponseDto> response = wordService.getCompositionWords();
        return SuccessResponse.of(response);
    }

}
