package com.going.server.domain.word.controller;

import com.going.server.domain.word.dto.AddRequestDto;
import com.going.server.domain.word.dto.ModifyRequestDto;
import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.service.WordService;
import com.going.server.domain.word.service.WordServiceImpl;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    //구성어휘 목록 조회
    @GetMapping()
    @Operation(summary = "구성어휘 목록 조회", description = "대표어휘의 기본키를 넣으면 구성 어휘와 대표어휘 유무가 담긴 배열을 반환합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "구성어휘 목록을 성공적으로 반환했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"FastAPI is running!\"}")
                    )
            )
    })
    public SuccessResponse<WordResponseDto> getWordList(@RequestParam Long clusterId){
        WordResponseDto dto = wordService.getWordList(clusterId);
        return SuccessResponse.of(dto);
    }

    //구성어휘 삭제
    @DeleteMapping("/{wordId}")
    @Operation(summary = "구성어휘 삭제", description = "삭제할 구성어휘를 Url에 넣고 요청하면 어휘가 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "구성어휘를 성공적으로 삭제했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"FastAPI is running!\"}")
                    )
            )
    })
    public SuccessResponse<?> deleteWord(@PathVariable Long wordId){
        wordService.deleteWord(wordId);
        return SuccessResponse.empty();
    }

    //구성어휘 수정
    @PatchMapping("/{wordId}")
    @Operation(summary = "구성어휘 수정", description = "수정할 구성어휘를 요청 body에 넣고 요청하면 어휘가 수정됩니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "구성어휘를 성공적으로 수정했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"FastAPI is running!\"}")
                    )
            )
    })
    public SuccessResponse<?> modifyWord(@PathVariable Long wordId, @RequestBody ModifyRequestDto dto) {
        wordService.modifyWord(wordId, dto);
        return SuccessResponse.empty();
    }

    //구성어휘 추가
    @PostMapping()
    @Operation(summary = "구성어휘 추가", description = "추가하고 싶은 어휘를 요청 body에 넣고 요청하면 어휘가 추가됩니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "구성어휘를 성공적으로 추가했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"FastAPI is running!\"}")
                    )
            )
    })
    public SuccessResponse<?> addWord(@RequestBody AddRequestDto dto) {
        wordService.addWord(dto);
        return SuccessResponse.empty();
    }
}
