package com.going.server.domain.sentence.controller;

import com.going.server.domain.sentence.service.SentenceService;
import com.going.server.domain.sentence.service.SentenceServiceImpl;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sentence")
@RequiredArgsConstructor
public class SentenceController {
    private final SentenceService sentenceService;

    @GetMapping("/{wordId}")
    @Operation(summary = "구성어휘가 사용된 문장 조회", description = "구성어휘의 기본키를 URL에 넣고 요청하면 해당 구성어휘가 사용된 문장이 담긴 배열을 반환합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "구성어휘가 사용된 문장을 성공적으로 반환했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"FastAPI is running!\"}")
                    )
            )
    })
    public SuccessResponse<List<String>> getSentence(@PathVariable Long wordId) {
        List<String> list = sentenceService.getSentence(wordId);
        return SuccessResponse.of(list);
    }
}
