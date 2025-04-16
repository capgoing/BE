package com.going.server.domain.quiz.controller;

import com.going.server.domain.graph.dto.nodeDetailDto;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@Tag(name="[캡스톤]Quiz", description = "퀴즈 관련 통신을 위한 API")
public class QuizController {

    @PostMapping("/{graphId}?mode={mode}")
    @Operation(summary = "[퀴즈화면] 퀴즈 생성", description = "퀴즈 화면에서 해당 모드의 퀴즈를 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "퀴즈를 성공적으로 생성하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터(mode 또는 graphId)가 올바르지 않습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 ID의 지식그래프를 찾을 수 없습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })
    public SuccessResponse<?> createQuiz(@PathVariable String graphId, @RequestParam String mode) {


    }

    // 퀴즈 결과 저장 API
}
