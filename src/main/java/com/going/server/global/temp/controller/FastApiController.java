package com.going.server.global.temp.controller;

import com.going.server.domain.word.dto.CompositionClusterResponseDto;
import com.going.server.domain.word.dto.CompositionWordResponseDto;
import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.entity.CompositionWord;
import com.going.server.global.response.SuccessResponse;
import com.going.server.global.temp.service.FastApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/words")
@Tag(name = "FastAPI 통신 테스트", description = "FastAPI 서버와의 통신을 테스트하는 API")
@Slf4j
public class FastApiController {

    private final FastApiService fastApiService;

    @GetMapping("/test-fastapi")
    @Operation(summary = "FastAPI 서버 테스트", description = "FastAPI 서버에 GET 요청을 보내고 정상적인 응답이 오는지 확인합니다.")
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
        String response = fastApiService.callFastApi();
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @Operation(summary = "FastAPI 클러스터 결과 조회", description = "FastAPI 서버에서 클러스터링 결과를 가져옵니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "FastAPI에서 클러스터링 데이터를 성공적으로 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{ \"message\": \"클러스터링 완료!\", \"clusters\": [{ \"cluster_id\": 0, \"word_sentences\": { \"정서진\": [ \"문장1\", \"문장2\" ] } }] }")
                    )
            )
    })
    public SuccessResponse<List<WordResponseDto>> getCluster() {
        return fastApiService.getCluster();
    }

    @GetMapping("/composition-word")
    public SuccessResponse<List<CompositionClusterResponseDto>> getCompositionWords(@RequestParam Long clusterId) {
        return fastApiService.getCompositionWords(clusterId);
    }

    @GetMapping("/{clusterId}/{compositionWord}")
    public SuccessResponse<List<String>> getSentence(@PathVariable Long clusterId, @PathVariable String compositionWord) {
        return fastApiService.getSentence(clusterId,compositionWord);
    }
}
