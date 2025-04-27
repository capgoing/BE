package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PictureQuizGenerator implements QuizGenerator<PictureQuizDto> {

    @Override
    public PictureQuizDto generate(Graph graph) {
        Random random = new Random();
        Set<String> shuffled = new HashSet<>(); // 보기로 제공할 랜덤 문장들(중복 방지)
        int shuffledListSize = 3; // 총 보기 수 (정답 포함)
        List<String> candidateSentences = new ArrayList<>(); // 전처리한 includeSentence

        // 그래프 노드를 돌면서 문장 후보 수집
        for (GraphNode node : graph.getNodes()) {
            // IncludeSentence가 비어있는 경우 넘어가기
            if (node.getIncludeSentence() == null || node.getIncludeSentence().isBlank()) continue;

            // "." 으로 문장 나누기
            String[] splitSentences = node.getIncludeSentence().split("\\.");

            for (String rawSentence : splitSentences) {
                String sentence = rawSentence.trim();
                if (sentence.isBlank()) continue; // 공백은 스킵
                if (sentence.split("\\s+").length < 5) continue; // 5단어 미만은 스킵

                candidateSentences.add(sentence);
            }
        }

        // 후보 문장이 부족할 경우 방어
        if (candidateSentences.size() < shuffledListSize) {
            throw new IllegalStateException("생성 가능한 문장이 부족합니다.");
        }

        // 랜덤으로 문장 선택
        Collections.shuffle(candidateSentences, random);

        // 보기용 문장 추가
        Set<String> selectedSentences = new LinkedHashSet<>();
        for (int i = 0; i < shuffledListSize; i++) {
            selectedSentences.add(candidateSentences.get(i));
        }

        // 무작위로 정답 보기 설정
        int answerIndex = random.nextInt(shuffledListSize);
        String answer = new ArrayList<>(selectedSentences).get(answerIndex);

        // TODO : 정답 보기 기반으로 이미지 생성


        // TODO : S3에 이미지 업로드

        // TODO : S3 업로드 후 이미지 URL 받아오기
        String imageUrl = "https://s3-ap-northeast-2.amazonaws.com/~.png"; // 예시 url

        return PictureQuizDto.builder()
                .imageUrl(imageUrl)
                .shuffled(selectedSentences)
                .answer(answer)
                .build();
    }
}
