package com.going.server.domain.cluster.service;

import com.going.server.domain.cluster.dto.ClusterDto;
import com.going.server.domain.cluster.dto.ClusterResponseDto;
import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClusterServiceImpl implements ClusterService {
    private final ClusterRepository clusterRepository;
    private final WordRepository wordRepository;

    @Override
    public ClusterResponseDto getCluster() {
        //클러스터 가져오기
        List<Cluster> clusters = clusterRepository.findAll();

        //클러스터링 결과 이미지
        //String imageUrl = clusters.get(1).getResultImg();

        //클러스터링 결과 리스트 생성
        List<ClusterDto> clusterDto = new ArrayList<>();
        clusters.forEach(cluster -> {
            List<Word> words = wordRepository.findByCluster_ClusterId(cluster.getClusterId());
            List<String> wordList = new ArrayList<>();
            for (Word word : words) {
                wordList.add(word.getComposeWord());
            }
            clusterDto.add(ClusterDto.from(
                    cluster.getClusterId(),
                    cluster.getRepresentWord(),
                    wordList
            ));
        });

        //응답 Dto 생성
        ClusterResponseDto clusterResponseDto = ClusterResponseDto.from(clusterDto,null);

        return clusterResponseDto;
    }
}
