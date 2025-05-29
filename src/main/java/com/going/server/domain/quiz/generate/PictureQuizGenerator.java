package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphRepository;
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
    private final GraphRepository graphRepository;

    @Override
    public PictureQuizDto generate(Graph currentGraph) {
        Random random = new Random();
        int shuffledListSize = 3;

        // 정답 후보 문장: 현재 그래프에서 수집
        List<String> answerCandidates = extractCandidateSentences(currentGraph);

        if (answerCandidates.isEmpty()) {
            throw new IllegalStateException("현재 그래프에서 사용할 수 있는 문장이 없습니다.");
        }

        // 정답 1개 선택
        Collections.shuffle(answerCandidates, random);
        String answer = answerCandidates.get(0);

        // 오답 후보 문장: 다른 그래프들에서 수집
        List<Graph> allGraphs = graphRepository.findAll();
        List<String> distractorCandidates = new ArrayList<>();
        for (Graph graph : allGraphs) {
            if (!graph.equals(currentGraph)) {
                distractorCandidates.addAll(extractCandidateSentences(graph));
            }
        }

        // 오답이 부족한 경우: 현재 그래프에서 추가 확보
        if (distractorCandidates.size() < shuffledListSize - 1) {
            distractorCandidates.addAll(answerCandidates.subList(1, Math.min(answerCandidates.size(), shuffledListSize)));
        }

        // 선지 셔플 및 정답 포함해서 뽑기
        Collections.shuffle(distractorCandidates, random);
        Set<String> selectedSentences = new LinkedHashSet<>();
        selectedSentences.add(answer);
        int index = 0;
        while (selectedSentences.size() < shuffledListSize && index < distractorCandidates.size()) {
            selectedSentences.add(distractorCandidates.get(index));
            index++;
        }

        if (selectedSentences.size() < shuffledListSize) {
            throw new IllegalStateException("문제가 될 문장이 충분하지 않습니다.");
        }

        String prompt = buildQuizImagePrompt(answer);
        ImageCreateRequestDto requestDto = new ImageCreateRequestDto(
                prompt,
                "dall-e-3",
                "vivid",
                "standard",
                "1024x1024",
                1
        );
        String imageUrl = imageCreateService.generatePicture(requestDto);

        return PictureQuizDto.builder()
                .imageUrl(imageUrl)
                .shuffled(selectedSentences)
                .answer(answer)
                .build();
    }

    // 문장 후보 추출 메서드 분리
    private List<String> extractCandidateSentences(Graph graph) {
        List<String> sentences = new ArrayList<>();

        for (GraphNode node : graph.getNodes()) {
            String raw = node.getIncludeSentence();
            if (raw == null || raw.isBlank()) continue;

            String[] split = raw.split("\\.");
            for (String s : split) {
                String trimmed = s.trim();
                if (trimmed.isBlank()) continue;
                if (trimmed.split("\\s+").length < 5) continue;
                sentences.add(trimmed);
            }
        }

        return sentences;
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
