package com.going.server.global.temp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import com.going.server.domain.sentence.entity.Sentence;
import com.going.server.domain.sentence.repository.SentenceRepository;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStreamReader;
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
            JsonNode root = mapper.readTree(is);

            for (JsonNode clusterNode : root) {
                Long clusterId = clusterNode.get("clusterId").asLong();
                String representWord = clusterNode.get("representWord").asText();
                String resultImg = null; // 필요 시 넣기

                // Cluster 저장
                Cluster cluster = Cluster.builder()
                        .clusterId(clusterId)
                        .representWord(representWord)
                        .resultImg(resultImg)
                        .build();
                clusterRepository.save(cluster);

                // Word 처리
                JsonNode words = clusterNode.get("words");
                JsonNode sentences = clusterNode.get("sentences");

                for (int i = 0; i < words.size(); i++) {
                    String wordStr = words.get(i).asText();

                    Word word = Word.builder()
                            .composeWord(wordStr)
                            .cluster(cluster)
                            .build();
                    wordRepository.save(word);

                    // 해당 단어에 해당하는 문장 있으면 Sentence 저장
                    if (i < sentences.size()) {
                        String sentenceStr = sentences.get(i).asText();
                        Sentence sentence = Sentence.builder()
                                .sentence(sentenceStr)
                                .word(word)
                                .build();
                        sentenceRepository.save(sentence);
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    public Word testWord(String word) {
        Word wordEntity = Word.toEntity(word, null);
        return wordRepository.save(wordEntity);

    }

}
