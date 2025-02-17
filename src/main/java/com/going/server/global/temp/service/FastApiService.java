package com.going.server.global.temp.service;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.domain.cluster.repository.ClusterRepository;
import com.going.server.domain.sentence.entity.Sentence;
import com.going.server.domain.sentence.repository.SentenceRepository;
import com.going.server.domain.word.entity.Word;
import com.going.server.domain.word.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FastApiService {

    private final WordRepository wordRepository;
    private final SentenceRepository sentenceRepository;
    @Value("${fastapi.base-url}")
    private String baseUrl;

    private final WebClient webClient;
    private final ClusterRepository clusterRepository;

    public FastApiService(WebClient.Builder webClientBuilder, ClusterRepository clusterResultRepository, WordRepository wordRepository, SentenceRepository sentenceRepository) {
        this.webClient = webClientBuilder.build();
        this.clusterRepository = clusterResultRepository;
        this.wordRepository = wordRepository;
        this.sentenceRepository = sentenceRepository;
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
    public void setCluster() {
        // FastAPI 요청 데이터 (필요시 변경)
        Map<String, Object> requestData = Map.of("input_text", "클러스터링할 데이터 예제");

        // FastAPI 응답 받기
        Map<String, Object> response = webClient.post()
                .uri(baseUrl + "/api/cluster")  // FastAPI 클러스터링 엔드포인트
                .bodyValue(requestData)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        //모든 클러스터링 결과 저장
        List<Map<String, Object>> clusters = (List<Map<String, Object>>) response.get("clusters");

        //클러스터링 결과 이미지 저장
        String imageUrl = response.get("image_url").toString();

        for (Map<String, Object> cluster : clusters) {
            Map<String, List<String>> wordSentences = (Map<String, List<String>>) cluster.get("word_sentences");

            if (wordSentences.isEmpty()) continue; // 빈 클러스터 예외 처리

            //첫 번째 단어를 대표 어휘로 설정
            String representWord = wordSentences.keySet().iterator().next();
            //엔티티 저장
            Cluster clusterEntity = Cluster.toEntity(representWord, imageUrl);
            //클러스터 결과 DB에 저장
            Cluster saveCluster = clusterRepository.save(clusterEntity);

            for (Map.Entry<String, List<String>> entry : wordSentences.entrySet()) {
                //단어
                String word = entry.getKey();
                //문장들
                List<String> sentences = entry.getValue();
                //Word 엔티티 생성
                Word wordEntity = Word.toEntity(word, saveCluster);
                //DB에 저장
                Word saveWord = wordRepository.save(wordEntity);
                for (String sentence : sentences) {
                    //Sentence 엔티티 생성
                    Sentence sententEntity = Sentence.toEntity(sentence, saveWord);
                    sentenceRepository.save(sententEntity);
                }
            }
        }
    }
}
