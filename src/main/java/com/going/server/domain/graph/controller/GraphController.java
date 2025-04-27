package com.going.server.domain.graph.controller;

import com.going.server.domain.graph.dto.*;
import com.going.server.domain.graph.service.GraphService;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
@RequiredArgsConstructor
@Tag(name = "[캡스톤]Graph", description = "지식그래프 관련 통신을 위한 API")
public class GraphController {
    private final GraphService graphService;

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
    public SuccessResponse<GraphListDto> getGraphList() {
        GraphListDto result = graphService.getGraphList();
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
    public SuccessResponse<?> deleteGraph(@PathVariable("graphId") String graphId) {
        graphService.deleteGraph(Long.parseLong(graphId));
        return SuccessResponse.empty();
    }

    @GetMapping("/{graphId}")
    @Operation(summary = "지식 그래프 전체 데이터(전체 화면) 조회", description = "현재 저장된 전체 지식 그래프 데이터를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "지식 그래프가 성공적으로 조회되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })
    public SuccessResponse<KnowledgeGraphDto> getGraph(@PathVariable("graphId") String graphId) {
        KnowledgeGraphDto result = graphService.getGraph(Long.parseLong(graphId));
        return SuccessResponse.of(result);
    }

    @GetMapping("/{graphId}/{nodeId}")
    @Operation(summary = "지식 그래프 상세 조회 (노드 상세 조회)", description = "특정 노드의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "노드가 성공적으로 조회되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })
    public SuccessResponse<NodeDto> getNode(@PathVariable("graphId") String graphId, @PathVariable("nodeId") String nodeId) {
        NodeDto result = graphService.getNode(Long.parseLong(graphId),Long.parseLong(nodeId));
        return SuccessResponse.of(result);
    }

    @PostMapping("/{graphId}")
    @Operation(summary = "노드 추가", description = "지식 그래프에서 노드를 추가합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "노드가 성공적으로 추가되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })

    public SuccessResponse<?> addNode(
            @PathVariable("graphId")
            String graphId,
            @RequestBody @Valid
            NodeAddDto dto
    ) {
        graphService.addNode(Long.parseLong(graphId),dto);
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{graphId}/{nodeId}")
    @Operation(summary = "노드 삭제", description = "지식 그래프에서 노드를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "노드가 성공적으로 삭제되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })

    public SuccessResponse<?> deleteNode(
            @PathVariable("graphId")
            String graphId,
            @PathVariable("nodeId")
            String nodeId
    ) {
        graphService.deleteNode(Long.parseLong(graphId), Long.parseLong(nodeId));
        return SuccessResponse.empty();
    }

    @PatchMapping("/{graphId}/{nodeId}")
    @Operation(summary = "노드 수정", description = "지식 그래프에서 노드를 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "노드가 성공적으로 수정되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })

    public SuccessResponse<?> modifyNode(
            @PathVariable("graphId")
            String graphId,
            @PathVariable("nodeId")
            String nodeId,
            @RequestBody
            @Valid
            NodeModifyDto dto
    ) {
        graphService.modifyNode(Long.parseLong(graphId), Long.parseLong(nodeId),dto);
        return SuccessResponse.empty();
    }

}
