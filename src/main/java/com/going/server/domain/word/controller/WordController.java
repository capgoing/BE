package com.going.server.domain.word.controller;

import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.service.WordService;
import com.going.server.domain.word.service.WordServiceImpl;
import com.going.server.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping("/words")
    public SuccessResponse<WordResponseDto> getWordList(@RequestParam Long clusterId){
        WordResponseDto dto = wordService.getWordList(clusterId);
        return SuccessResponse.of(dto);
    }
}
