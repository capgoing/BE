package com.going.server.global.temp.controller;

import com.going.server.global.temp.service.FastApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "FastAPI 통신 테스트", description = "FastAPI 서버와의 통신을 테스트하는 API")
public class FastApiController {
    private final FastApiService fastApiService;
    @GetMapping("/test-fastapi")
    @Operation(
            summary = "FastAPI 서버 테스트",
            description = "FastAPI 서버에 GET 요청을 보내고 정상적인 응답이 오는지 확인합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "FastAPI 서버가 정상적으로 응답을 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"FastAPI is running!\"}")
                    )
            )
    })
    public ResponseEntity<String> testFastApi() {
        String response =  fastApiService.callFastApi();
        return ResponseEntity.ok(response);
    }
}
