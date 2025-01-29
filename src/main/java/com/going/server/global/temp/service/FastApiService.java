package com.going.server.global.temp.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.going.server.domain.word.dto.CompositionClusterResponseDto;
import com.going.server.domain.word.dto.CompositionWordResponseDto;
import com.going.server.domain.word.dto.WordResponseDto;
import com.going.server.domain.word.entity.CompositionWord;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import com.going.server.global.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FastApiService {

    private final WordRepository wordRepository;
    @Value("${fastapi.base-url}")
    private String baseUrl;

    private final WebClient webClient;

    public FastApiService(WebClient.Builder webClientBuilder, WordRepository wordRepository) {
        this.webClient = webClientBuilder.build();
        this.wordRepository = wordRepository;
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
     * FastAPI 서버에 클러스터링 요청 (POST 요청)
     */
    public SuccessResponse<List<WordResponseDto>> getCluster() {
        Map response = webClient.post()
                .uri(baseUrl + "/api/cluster")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("clusters")) {
            return null;
        }

        List<Map<String, Object>> clusters = (List<Map<String, Object>>) response.get("clusters");
        String imageUrl = response.get("image_url").toString();
        List<Word> wordEntities = new ArrayList<>();
        List<WordResponseDto> responseDtos = new ArrayList<>();

        for (Map<String, Object> cluster : clusters) {
            Integer clusterId = (Integer) cluster.get("cluster_id"); // 클러스터 ID 가져오기
            Map<String, List<String>> wordSentences = (Map<String, List<String>>) cluster.get("word_sentences");

            if (wordSentences != null && !wordSentences.isEmpty()) {
                String firstKey = wordSentences.keySet().iterator().next(); // 첫 번째 키 가져오기
                Word wordEntity = new Word(firstKey);
                wordEntities.add(wordEntity);
                responseDtos.add(new WordResponseDto(clusterId, firstKey,imageUrl)); // 첫 번째 키와 클러스터 ID 저장
            }
        }

        if (!wordEntities.isEmpty()) {
            wordRepository.saveAll(wordEntities); // MongoDB에 첫 번째 키만 저장
        }

        return SuccessResponse.of(responseDtos);
    }

    public SuccessResponse<List<CompositionClusterResponseDto>> getCompositionWords(Long clusterId) {
        Map response = webClient.post()
                .uri(baseUrl + "/api/cluster")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("clusters")) {
            return null;
        }

        List<Map<String, Object>> clusters = (List<Map<String, Object>>) response.get("clusters");
        List<CompositionClusterResponseDto> clusterResponseList = new ArrayList<>();

        for (Map<String, Object> cluster : clusters) {
            Integer currentClusterId = (Integer) cluster.get("cluster_id"); // 클러스터 ID 가져오기

            // 파라미터로 받은 clusterId와 다르면 건너뛴다.
            if (clusterId != null && !clusterId.equals(Long.valueOf(currentClusterId))) {
                continue;
            }

            Map<String, List<String>> wordSentences = (Map<String, List<String>>) cluster.get("word_sentences");
            List<CompositionWordResponseDto> wordResponseList = new ArrayList<>();

            if (wordSentences != null && !wordSentences.isEmpty()) {
                for (String key : wordSentences.keySet()) { // 모든 키 저장
                    CompositionWord compositionWord = new CompositionWord(key);
                    wordResponseList.add(CompositionWordResponseDto.of(compositionWord));
                }
            }

            clusterResponseList.add(new CompositionClusterResponseDto(currentClusterId, wordResponseList));
        }

        return SuccessResponse.of(clusterResponseList);
    }


    public SuccessResponse<List<String>> getSentence(Long clusterId, String compositionWord) {
        Map response = webClient.post()
                .uri(baseUrl + "/api/cluster")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("clusters")) {
            System.out.println("❌ 클러스터 데이터 없음!");
            return SuccessResponse.of(Collections.emptyList());
        }

        List<Map<String, Object>> clusters = (List<Map<String, Object>>) response.get("clusters");

        System.out.println("✅ FastAPI 응답 데이터: " + clusters);

        for (Map<String, Object> cluster : clusters) {
            Integer currentClusterId = (Integer) cluster.get("cluster_id");

            System.out.println("🔹 현재 클러스터 ID: " + currentClusterId);

            // 클러스터 ID가 요청된 clusterId와 일치하는지 확인
            if (!clusterId.equals(Long.valueOf(currentClusterId))) {
                continue;
            }

            Map<String, List<String>> wordSentences = (Map<String, List<String>>) cluster.get("word_sentences");

            if (wordSentences == null) {
                System.out.println("❌ wordSentences 데이터 없음!");
                return SuccessResponse.of(Collections.emptyList());
            }

            System.out.println("✅ 현재 클러스터의 wordSentences: " + wordSentences.keySet());

            // 단어가 존재하는지 확인
            if (wordSentences.containsKey(compositionWord)) {
                System.out.println("✅ 요청된 단어 (" + compositionWord + ") 의 문장 배열 반환: " + wordSentences.get(compositionWord));
                return SuccessResponse.of(wordSentences.get(compositionWord));
            } else {
                System.out.println("❌ 요청된 단어 (" + compositionWord + ") 가 존재하지 않음!");
            }
        }

        // 데이터가 없을 경우 빈 리스트 반환
        return SuccessResponse.of(Collections.emptyList());
    }
}
