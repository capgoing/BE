package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.openai.dto.ImageCreateRequestDto;
import com.going.server.domain.openai.service.ImageCreateService;
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
        int index = 0;
        while (selectedSentences.size() < shuffledListSize && index < candidateSentences.size()) {
            selectedSentences.add(candidateSentences.get(index));
            index++;
        }

        // 무작위로 정답 보기 설정
        int answerIndex = random.nextInt(shuffledListSize);
        String answer = new ArrayList<>(selectedSentences).get(answerIndex);

        String prompt = buildQuizImagePrompt(answer);
        ImageCreateRequestDto requestDto = new ImageCreateRequestDto(
                prompt,
                "dall-e-3",
                "vivid",
                "900x900",
                1);
        String imageUrl = imageCreateService.generatePicture(requestDto);

        return PictureQuizDto.builder()
                .imageUrl(imageUrl)
                .shuffled(selectedSentences)
                .answer(answer)
                .build();
    }

    // 이미지 생성 프롬프트 생성 메서드
   public static String buildQuizImagePrompt(String answer) {
        return "You are given an educational description in natural language.\n\n" +
                "1. First, analyze the sentence to determine what kind of relationship it contains, such as:\n" +
                "- Cause and effect\n" +
                "- Inclusion or category\n" +
                "- Example and concept\n" +
                "- Behavioral actions\n" +
                "- General explanation\n\n" +
                "2. Then, generate a cute, warm, and educational diagram-style illustration that reflects the structure and meaning of the sentence.\n\n" +
                "Use flat vector illustrations inspired by iOS emojis, with bright and soft colors.\n\n" +
                "If the sentence includes multiple ideas or relationships, use simple visual symbols like arrows or grouping layouts to represent those relationships **only when necessary**.\n" +
                "Do **not overuse symbols**—use them only when they help express meaning clearly.\n\n" +
                "Do not include any text or labels in the image. Use only visuals.\n\n" +
                "[Description]\n" + answer;
    }

    // 네컷 만화 생성 프롬프트 빌드 메서드

}
