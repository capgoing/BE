package com.going.server.domain.cluster.service;

import com.going.server.domain.cluster.dto.ClusterResponseDto;
import com.going.server.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ClusterServiceImpl implements ClusterService {
    @Override
    public List<ClusterResponseDto> getCluster() {

        return List.of();
    }
}
