package com.going.server.domain.sentence.controller;

import com.going.server.domain.sentence.service.SentenceService;
import com.going.server.domain.sentence.service.SentenceServiceImpl;
import com.going.server.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SentenceController {
    private final SentenceService sentenceService;

    @GetMapping("/sentence/{wordId}")
    public SuccessResponse<List<String>> getSentence(@PathVariable Long wordId) {
        List<String> list = sentenceService.getSentence(wordId);
        return SuccessResponse.of(list);
    }
}
