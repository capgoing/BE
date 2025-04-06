package com.going.server.global.temp.service;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import com.going.server.domain.sentence.entity.Sentence;
import com.going.server.domain.sentence.repository.SentenceRepository;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class FastApiService {

    private final WordRepository wordRepository;
    private final SentenceRepository sentenceRepository;
    @Value("${fastapi.base-url}")
    private String baseUrl;
    private final WebClient.Builder webClientBuilder;
    private final ClusterRepository clusterRepository;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.build();
    }



    /**
     * FastAPI 서버의 기본 상태 확인 (GET 요청)
     */
    public String callFastApi() {
        return webClient.get()
                .uri(baseUrl + "/")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * FastAPI에서 클러스터링 결과 가져와 DB에 저장 (POST 요청)
     */

    @Profile("!test")
    public void setCluster() {
        Map<String, Object> requestData = Map.of("input_text", "클러스터링할 데이터 예제");

        Map<String, Object> response = webClient.post()
                .uri(baseUrl + "/api/cluster")
                .bodyValue(requestData)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> clusters = (List<Map<String, Object>>) response.get("clusters");
        String imageUrl = response.get("image_url").toString();

        // 병렬처리를 위한 리스트
        List<Cluster> clusterEntities = new ArrayList<>();
        List<Word> wordEntities = new ArrayList<>();
        List<Sentence> sentenceEntities = new ArrayList<>();

        for (Map<String, Object> cluster : clusters) {
            Map<String, List<String>> wordSentences = (Map<String, List<String>>) cluster.get("word_sentences");

            if (wordSentences.isEmpty()) continue;

            String representWord = wordSentences.keySet().iterator().next();
            Cluster clusterEntity = Cluster.toEntity(representWord, imageUrl);
            clusterEntities.add(clusterEntity);

            for (Map.Entry<String, List<String>> entry : wordSentences.entrySet()) {
                Word wordEntity = Word.toEntity(entry.getKey(), clusterEntity);
                wordEntities.add(wordEntity);

                for (String sentence : entry.getValue()) {
                    sentenceEntities.add(Sentence.toEntity(sentence, wordEntity));
                }
            }
        }

        // 1. 클러스터 저장
        clusterRepository.saveAll(clusterEntities);

        // 2. 클러스터 저장 후 Word에 연결된 객체들을 저장
        wordRepository.saveAll(wordEntities);
        sentenceRepository.saveAll(sentenceEntities);
    }


    public Word testWord(String word) {
        Word wordEntity = Word.toEntity(word, null);
        return wordRepository.save(wordEntity);

    }

}
