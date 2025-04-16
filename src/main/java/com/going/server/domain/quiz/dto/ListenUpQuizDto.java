package com.going.server.domain.quiz.dto;

import lombok.Builder;
import java.util.List;

public class ListenUpQuizDto {
    private List<ListenUpQuiz> quizzes;

    @Builder
    public static class ListenUpQuiz {
        private List<String> shuffled;
        private List<String> answer;
        private String description;
    }
}
