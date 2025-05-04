package com.going.server.domain.history.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@Tag(name = "[1차구현]History", description = "수정사항 조회 통신을 위한 API")
public class HistoryController {

}
