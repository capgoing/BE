package com.going.server.domain.cluster.controller;

import com.going.server.domain.cluster.dto.ClusterResponseDto;
import com.going.server.domain.cluster.service.ClusterService;
import com.going.server.domain.cluster.service.ClusterServiceImpl;
import com.going.server.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cluster")
@RequiredArgsConstructor
public class ClusterController {
    private final ClusterService clusterServic;
    //클러스터링 결과 조회 ()
    @GetMapping()
    public SuccessResponse<List<ClusterResponseDto>> getCluster() {
        List<ClusterResponseDto> response = clusterServic.getCluster();
        return SuccessResponse.of(response);

    }
}
