package com.going.server.domain.quiz.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ListenUpQuizDto {
    private List<ListenUpQuiz> quizzes;

    @Builder
    @Getter
    public static class ListenUpQuiz {
        private List<String> shuffled;
        private List<String> answer;
        private String description;
    }
}
