package com.going.server.domain.cluster.controller;

import com.going.server.domain.cluster.dto.ClusterResponseDto;
import com.going.server.domain.cluster.service.ClusterService;
import com.going.server.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClusterController {
    private final ClusterService clusterServic;
    //대표어휘 조회
    @GetMapping("/cluster")
    public SuccessResponse<ClusterResponseDto> getCluster() {
        ClusterResponseDto response = clusterServic.getCluster();
        return SuccessResponse.of(response);
    }
}
