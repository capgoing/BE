package com.going.server.domain.word.service;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import com.going.server.domain.word.dto.WordDto;
import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final ClusterRepository clusterRepository;

    @Override
    public WordResponseDto getWordList(Long clusterId) {
        Optional<Cluster> cluster = clusterRepository.findById(clusterId);
        //TODO: 존재하지 않는 클러스터 예외처리

        //요청한 클러스터의 기본키로 구성어휘 찾기
        List<Word> words = wordRepository.findByCluster_ClusterId(clusterId);
        List<WordDto> wordDto = new ArrayList<>();
        words.forEach(word -> {
            Boolean isRepresent = cluster.get().getRepresentWord().equals(word.getComposeWord());
            wordDto.add(WordDto.from(word.getComposeWord(),isRepresent));
        });
        WordResponseDto wordResponseDto = WordResponseDto.of(cluster.get().getClusterId(),wordDto);
        return wordResponseDto;
    }
}
