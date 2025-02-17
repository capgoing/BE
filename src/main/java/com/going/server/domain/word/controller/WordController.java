package com.going.server.domain.word.controller;

import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.service.WordService;
import com.going.server.domain.word.service.WordServiceImpl;
import com.going.server.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    //구성어휘 목록 조회
    @GetMapping("/words")
    public SuccessResponse<WordResponseDto> getWordList(@RequestParam Long clusterId){
        WordResponseDto dto = wordService.getWordList(clusterId);
        return SuccessResponse.of(dto);
    }

    //TODO: 구성어휘 삭제
    @DeleteMapping("/words/{wordId}")
    public SuccessResponse<?> deleteWord(@PathVariable Long wordId){
        wordService.deleteWord(wordId);
        return SuccessResponse.empty();
    }

    //TODO: 구성어휘 수정
}
