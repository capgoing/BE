package com.going.server.domain.word.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {
    @GetMapping()
    public ResponseEntity<?> getWords() {
        return null;
    }

}
