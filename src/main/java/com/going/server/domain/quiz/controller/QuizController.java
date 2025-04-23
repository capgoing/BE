package com.going.server.domain.quiz.controller;

import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import com.going.server.domain.quiz.service.QuizServiceImpl;
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
@Tag(name="[캡스톤]ListenUpQuiz", description = "퀴즈 관련 통신을 위한 API")
public class QuizController {

    private QuizServiceImpl quizService;

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
            )
    })
    public SuccessResponse<?> createQuiz(@PathVariable String graphId, @RequestParam String mode) {
        QuizCreateResponseDto result = quizService.quizCreate(graphId, mode);
        return SuccessResponse.of(result);
    }

}
