package com.going.server.domain.cluster.service;

import com.going.server.domain.cluster.dto.ClusterDto;
import com.going.server.domain.cluster.dto.ClusterResponseDto;
import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClusterServiceImpl implements ClusterService {
    private final ClusterRepository clusterRepository;

    @Override
    public ClusterResponseDto getCluster() {
        //클러스터 가져오기
        List<Cluster> clusters = clusterRepository.findAll();

        //클러스터링 결과 이미지
        String imageUrl = clusters.get(0).getResultImg();

        //클러스터링 결과 리스트 생성
        List<ClusterDto> clusterDto = new ArrayList<>();
        clusters.forEach(cluster -> {
            clusterDto.add(ClusterDto.from(
                    cluster.getClusterId(),
                    cluster.getRepresentWord()
            ));
        });

        //응답 Dto 생성
        ClusterResponseDto clusterResponseDto = ClusterResponseDto.from(clusterDto,imageUrl);

        return clusterResponseDto;
    }
}
