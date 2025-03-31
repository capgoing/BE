package com.going.server.domain.word.service;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import com.going.server.domain.history.entity.History;
import com.going.server.domain.history.repository.HistoryRepository;
import com.going.server.domain.word.dto.AddRequestDto;
import com.going.server.domain.word.dto.ModifyRequestDto;
import com.going.server.domain.word.dto.WordDto;
import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final ClusterRepository clusterRepository;
    private final HistoryRepository historyRepository;

    @Override
    public WordResponseDto getWordList(Long clusterId) {

//        Cluster cluster = clusterRepository.getByCluster(clusterId);
//
//        //요청한 클러스터의 기본키로 구성어휘 찾기
//        List<Word> words = wordRepository.findByCluster_ClusterId(cluster.getClusterId());
//        List<WordDto> wordDto = new ArrayList<>();
//        words.forEach(word -> {
//            Boolean isRepresent = cluster.getRepresentWord().equals(word.getComposeWord());
//            wordDto.add(WordDto.from(word.getWordId(),word.getComposeWord(),isRepresent));
//        });
//        WordResponseDto wordResponseDto = WordResponseDto.of(cluster.getClusterId(),wordDto);
//        return wordResponseDto;
        return null;
    }

    @Override
    public void deleteWord(Long wordId) {
//        Word word = wordRepository.getByWord(wordId);
//        //변경사항 저장
//        History history = History.toEntity(word.getComposeWord(),"",word);
////        historyRepository.save(history);
//        wordRepository.delete(word);
    }

    @Transactional
    @Override
    public void modifyWord(Long wordId, ModifyRequestDto dto) {
//        Word findWord = wordRepository.getByWord(wordId);
//        String modifiedWord = dto.getWord();
//
//        //변경사항 저장
//        History history = History.toEntity(findWord.getComposeWord(),modifiedWord,findWord);
//        historyRepository.save(history);
//
//        findWord.setComposeWord(dto.getWord());
//        wordRepository.save(findWord);
    }

    @Override
    public void addWord(AddRequestDto dto) {
        String word = dto.getWord();
        Cluster cluster = clusterRepository.getByCluster(dto.getClusterId());
        Word newWord = Word.toEntity(word,cluster);
        //변경사항 저장
        History history = History.toEntity("",word,newWord);
        wordRepository.save(newWord);
        historyRepository.save(history);
    }
}
