package com.going.server.domain.sentence.service;

import com.going.server.domain.sentence.entity.Sentence;
import com.going.server.domain.sentence.repository.SentenceRepository;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SentenceServiceImpl implements SentenceService{
    private final SentenceRepository sentenceRepository;
    private final WordRepository wordRepository;

    @Override
    public List<String> getSentence(Long wordId) {
        Word word = wordRepository.getByWord(wordId);
        List<Sentence> sentences = sentenceRepository.findByWord_Id(word.getId());
        List<String> sentenceList = new ArrayList<>();
        sentences.forEach(sentence -> {
            sentenceList.add(sentence.getSentence());
        });
        return sentenceList;
    }
}
