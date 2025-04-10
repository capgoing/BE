package com.going.server.domain.cluster.controller;

import com.going.server.domain.cluster.dto.ClusterResponseDto;
import com.going.server.domain.cluster.service.ClusterService;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cluster")
@RequiredArgsConstructor
@Tag(name = "[1차구현]Cluster", description = "클러스터링 관련 통신을 위한 API")
public class ClusterController {
    private final ClusterService clusterServic;
    //대표어휘 조회
    @GetMapping()
    @Operation(summary = "대표어휘 조회", description = "클러스터링된 군집(대표어휘)과 클러스터링 결과 그래프 이미지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "대표어휘와 클러스터링 결과를 성공적으로 반환했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"FastAPI is running!\"}")
                    )
            )
    })
    public SuccessResponse<ClusterResponseDto> getCluster() {
        ClusterResponseDto response = clusterServic.getCluster();
        return SuccessResponse.of(response);
    }
}
