package com.going.server.domain.word.service;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import com.going.server.domain.word.dto.AddRequestDto;
import com.going.server.domain.word.dto.ModifyRequestDto;
import com.going.server.domain.word.dto.WordDto;
import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

        Cluster cluster = clusterRepository.getByCluster(clusterId);

        //요청한 클러스터의 기본키로 구성어휘 찾기
        List<Word> words = wordRepository.findByCluster_ClusterId(cluster.getClusterId());
        List<WordDto> wordDto = new ArrayList<>();
        words.forEach(word -> {
            Boolean isRepresent = cluster.getRepresentWord().equals(word.getComposeWord());
            wordDto.add(WordDto.from(word.getWordId(),word.getComposeWord(),isRepresent));
        });
        WordResponseDto wordResponseDto = WordResponseDto.of(cluster.getClusterId(),wordDto);
        return wordResponseDto;
    }

    @Override
    public void deleteWord(Long wordId) {
        Word word = wordRepository.getByWord(wordId);
        wordRepository.delete(word);
    }

    @Transactional
    @Override
    public void modifyWord(Long wordId, ModifyRequestDto dto) {
        Word findWord = wordRepository.getByWord(wordId);
        findWord.setComposeWord(dto.getWord());
        wordRepository.save(findWord);
    }

    @Override
    public void addWord(AddRequestDto dto) {
        String word = dto.getWord();
        Cluster cluster = clusterRepository.getByCluster(dto.getClusterId());
        Word newWord = Word.toEntity(word,cluster);
        wordRepository.save(newWord);
    }
}
