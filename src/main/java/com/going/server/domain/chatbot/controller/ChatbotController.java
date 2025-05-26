package com.going.server.domain.chatbot.controller;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.chatbot.service.ChatbotService;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@Tag(name = "[캡스톤]Chatbot", description = "챗봇 관련 기능 API")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/{graphId}/original")
    @Operation(summary = "[챗봇] 원문 보기", description = "그래프에 포함된 원문 텍스트를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "원문 반환 성공", content = @Content(schema = @Schema(implementation = CreateChatbotResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "그래프를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public SuccessResponse<?> getOriginalText(@PathVariable String graphId) {
        CreateChatbotResponseDto response = chatbotService.getOriginalText(graphId);
        return SuccessResponse.of(response, "201");
    }

    @PostMapping("/{graphId}/summary")
    @Operation(summary = "[챗봇] 요약본 생성하기", description = "그래프의 요약 정보를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "요약본 생성 성공", content = @Content(schema = @Schema(implementation = CreateChatbotResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "그래프를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public SuccessResponse<?> getSummaryText(@PathVariable String graphId) {
        CreateChatbotResponseDto response = chatbotService.getSummaryText(graphId);
        return SuccessResponse.of(response, "201");
    }


    @PostMapping("/{graphId}")
    @Operation(
            summary = "[챗봇 기능] 챗봇 응답 생성",
            description = """
                사용자의 질문에 따라 다양한 응답 모드를 제공합니다.
                - `default` : 일반 응답 (기존 GPT 방식)
                - `rag` : 지식그래프 기반 RAG 응답
                - `cartoon` : 4컷 만화 이미지 생성
                - `video` : 질문 주제에 맞는 교육 영상 추천
                """
    )
    @Parameters({
            @Parameter(name = "graphId", in = ParameterIn.PATH, description = "그래프 ID", required = true),
            @Parameter(name = "mode", in = ParameterIn.QUERY, description = "응답 모드 (default, rag, cartoon, video)", required = false)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "응답 생성 성공", content = @Content(schema = @Schema(implementation = CreateChatbotResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public SuccessResponse<?> generateChatbotAnswer(
            @PathVariable String graphId,
            @RequestParam(defaultValue = "default") String mode,
            @RequestBody(required = false) CreateChatbotRequestDto dto
    ) {
        CreateChatbotResponseDto result;

        switch (mode) {
            case "rag":
                result = chatbotService.createAnswerWithRAG(graphId, dto);
                break;
            case "cartoon":
                result = chatbotService.createCartoon(graphId, dto);
                break;
            case "video":
                result = chatbotService.recommendVideo(graphId, dto);
                break;
            case "default":
            default:
                result = chatbotService.createSimpleAnswer(graphId, dto);
        }

        return SuccessResponse.of(result, "201");
    }
}
