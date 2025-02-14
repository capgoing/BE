package com.going.server.domain.cluster.service;

import com.going.server.domain.cluster.dto.ClusterResponseDto;
import com.going.server.global.response.SuccessResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ClusterService {
    List<ClusterResponseDto> getCluster();
}
