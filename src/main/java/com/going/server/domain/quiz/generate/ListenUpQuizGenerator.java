package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.quiz.dto.ListenUpQuizDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ListenUpQuizGenerator implements QuizGenerator<ListenUpQuizDto> {

    @Override
    public ListenUpQuizDto generate(Graph graph) {
        Random random = new Random();
        List<ListenUpQuizDto.ListenUpQuiz> quizzes = new ArrayList<>();
        Set<String> usedSentences = new HashSet<>();
        List<String> options = new ArrayList<>();

        // 1. 그래프 노드에서 문장 추출
        for (GraphNode node : graph.getNodes()) {
            // IncludeSentence가 비어있는 경우 넘어가기
            if (node.getIncludeSentence() == null || node.getIncludeSentence().isBlank()) continue;

            // "." 으로 문장 나누기
            String[] splitSentences = node.getIncludeSentence().split("\\.");

            for (String rawSentence : splitSentences) {
                String sentence = rawSentence.trim();
                if (sentence.isBlank()) continue; // 공백은 스킵
                if (usedSentences.contains(sentence)) continue;

                String[] words = sentence.split("\\s+");
                if (words.length < 5) continue; // 5단어 미만은 스킵

                options.add(sentence);
            }
        }

        // 2. 단어 수 기준 정렬 (5단어에 가까운 순서)
        options.sort(Comparator.comparingInt(
                s -> Math.abs(s.trim().split("\\s+").length - 5)
        ));

        int count = 0;

        for (String sentence : options) {
            if (count >= 3) break;

            String[] words = sentence.split("\\s+");

            List<String> answer = new ArrayList<>();

            if (words.length == 5) { // 5단어면 그대로
                answer = Arrays.asList(words);
            } else {
                // 6단어 이상이면 랜덤하게 5개로 압축
                int mergeCount = words.length - 5; // 합쳐야 할 횟수
                List<String> wordList = new ArrayList<>(Arrays.asList(words));

                for (int i = 0; i < mergeCount; i++) {
                    int mergeIdx = random.nextInt(wordList.size() - 1); // 마지막 단어는 제외
                    String merged = wordList.get(mergeIdx) + " " + wordList.get(mergeIdx + 1);
                    wordList.set(mergeIdx, merged);
                    wordList.remove(mergeIdx + 1);
                }
                answer = wordList;
            }

            if (answer.size() != 5) continue; // 안전망

            List<String> shuffled = new ArrayList<>(answer);
            Collections.shuffle(shuffled, random);

            // 퀴즈 생성
            ListenUpQuizDto.ListenUpQuiz quiz = ListenUpQuizDto.ListenUpQuiz.builder()
                    .answer(answer)
                    .shuffled(shuffled)
                    .description(sentence) // 이 문장 전체가 TTS로 읽힐 문장
                    .build();

            quizzes.add(quiz);
            usedSentences.add(sentence);
            count++;
        }

        // 최종 퀴즈 DTO에 담아서 번환
        return ListenUpQuizDto.builder()
                .quizzes(quizzes)
                .build();
    }

}