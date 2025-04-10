package com.going.server.domain.graph.controller;

import com.going.server.domain.graph.dto.graphDto;
import com.going.server.domain.graph.dto.graphListDto;
import com.going.server.domain.graph.service.graphService;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
@RequiredArgsConstructor
public class graphController {
    private final graphService graphService;

    @GetMapping()
    @Operation(summary = "[메인화면] 그래프 리스트 조회", description = "메인화면에서 그래프 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "그래프 리스트를 성공적으로 반환했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })
    public SuccessResponse<graphListDto> getGraphList() {
        graphListDto result = graphService.getGraphList();
        return SuccessResponse.of(result);
    }

    @DeleteMapping("/{graphId}")
    @Operation(summary = "[메인화면] pdf파일 (지식그래프) 삭제", description = "워크 스페이스에서 지식 그래프 자체를 삭제하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "그래프가 성공적으로 삭제되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })
    public SuccessResponse<?> deleteGraph(@PathVariable("graphId") Long graphId) {
        graphService.deleteGraph(graphId);
        return SuccessResponse.empty();
    }

}
