package com.going.server.domain.chatbot.controller;

import com.going.server.domain.chatbot.dto.CreateChatbotRequestDto;
import com.going.server.domain.chatbot.dto.CreateChatbotResponseDto;
import com.going.server.domain.chatbot.service.ChatbotService;
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
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@Tag(name = "[캡스톤]Chatbot", description = "챗봇 관련 통신을 위한 API")
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping("/{graphId}")
    @Operation(summary = "[챗봇 화면] 챗봇 응답 생성", description = "챗봇 화면에서 사용자의 질문에 응답을 생성합니다.")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "201",
                    description = "호출에 성공했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    )
    public SuccessResponse<?> generateChatbotAnswer(@PathVariable String graphId,
                                                    @RequestBody CreateChatbotRequestDto createChatbotRequestDto) {
        CreateChatbotResponseDto result = chatbotService.createAnswer(graphId, createChatbotRequestDto);
        return SuccessResponse.of(result, "201");
    }
}
