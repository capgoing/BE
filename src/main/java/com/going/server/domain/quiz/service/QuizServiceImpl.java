package com.going.server.domain.quiz.service;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.GraphNotFoundException;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.quiz.dto.ConnectQuizDto;
import com.going.server.domain.quiz.dto.ListenUpQuizDto;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import com.going.server.domain.quiz.dto.QuizCreateResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService{
    private final GraphRepository graphRepository;

    @Override
    public QuizCreateResponseDto quizCreate(String graphIdStr, String mode) {
        Long graphId = Long.valueOf(graphIdStr);

        // 404 : 지식그래프 찾을 수 없음
        Graph graph = graphRepository.findById(graphId)
                .orElseThrow(GraphNotFoundException::new);

        Object quizDto = null;

        switch (mode) {
            case "listenUp":
                quizDto = listenUpQuizCreate(graph);
                break;
            case "connect":
                quizDto = connectQuizCreate(graph);
                break;
            case "picture":
                quizDto = pictureQuizCreate(graph);
                break;
            default:
                // TODO : 퀴즈 모드 관련 예외처리 필요
        }

        return new QuizCreateResponseDto<>(graphIdStr, mode, quizDto);
    }

    // listenUp 퀴즈 생성 메서드
    private ListenUpQuizDto listenUpQuizCreate(Graph graph) {
        Random random = new Random();
        List<ListenUpQuizDto.ListenUpQuiz> quizzes = new ArrayList<>();
        Set<String> usedSentences = new HashSet<>();
        List<String> candidates = new ArrayList<>();

        // 1. 그래프 노드에서 문장 추출
        for (GraphNode node : graph.getNodes()) {
            if (node.getIncludeSentence() == null || node.getIncludeSentence().isBlank()) continue;

            // "." 으로 문장 나누기
            String[] splitSentences = node.getIncludeSentence().split("\\.");

            for (String rawSentence : splitSentences) {
                String sentence = rawSentence.trim();
                if (sentence.isBlank()) continue; // 공백은 스킵
                if (usedSentences.contains(sentence)) continue;

                String[] words = sentence.split("\\s+");
                if (words.length < 5) continue; // 5단어 미만은 스킵

                candidates.add(sentence);
            }
        }

        // 2. 단어 수 기준 정렬 (5단어에 가까운 순서)
        candidates.sort(Comparator.comparingInt(
                s -> Math.abs(s.trim().split("\\s+").length - 5)
        ));

        int count = 0;

        for (String sentence : candidates) {
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

    // connect 퀴즈 생성 메서드
    private ConnectQuizDto connectQuizCreate(Graph graph) {
        // TODO : connect 퀴즈 생성 로직 작성
        return ConnectQuizDto.builder()
                .build();
    }

    // picture 퀴즈 생성 메서드
    private PictureQuizDto pictureQuizCreate(Graph graph) {
        // TODO : picture 퀴즈 생성 로직 작성
        return PictureQuizDto.builder()
                .build();
    }
}