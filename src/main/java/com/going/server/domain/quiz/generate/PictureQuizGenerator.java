package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.openai.ImageCreateService;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class PictureQuizGenerator implements QuizGenerator<PictureQuizDto> {
    private final ImageCreateService imageCreateService;

    @Override
    public PictureQuizDto generate(Graph graph) {
        Random random = new Random();
        Set<String> shuffled = new HashSet<>(); // 보기로 제공할 랜덤 문장들(중복 방지)
        int shuffledListSize = 3; // 총 보기 수 (정답 포함)
        List<String> candidateSentences = new ArrayList<>(); // includeSentence 바로 사용

        // 그래프 노드를 돌면서 문장 후보 수집
        for (GraphNode node : graph.getNodes()) {
            // IncludeSentence가 비어있는 경우 넘어가기
            if (node.getIncludeSentence() == null || node.getIncludeSentence().isBlank()) continue;

            String sentence = node.getIncludeSentence().trim();
            if (sentence.isBlank()) continue; // 공백은 스킵
            if (sentence.split("\\s+").length < 5) continue; // 5단어 미만은 스킵

            candidateSentences.add(sentence);
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

        // 정답 보기 기반으로 이미지 생성
        String prompt = buildImagePrompt(answer);
        String imageUrl = imageCreateService.generatePicture(prompt);

        return PictureQuizDto.builder()
                .imageUrl(imageUrl)
                .shuffled(selectedSentences)
                .answer(answer)
                .build();
    }

    // 이미지 생성 프롬프트 생성 메서드
    private String buildImagePrompt(String answer) {
        return "다음 문장을 그림으로 표현해주세요.\n"
                + "이모지를 최대한 활용하고, 깔끔한 스타일로 만들어주세요.\n"
                + "글자는 사용하지 말고, 그림으로만 표현해주세요.\n\n"
                + "[문장]\n" + answer;
    }
}
