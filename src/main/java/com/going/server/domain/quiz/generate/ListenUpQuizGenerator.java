package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.quiz.dto.ListenUpQuizDto;
import org.springframework.stereotype.Component;

@Component
public class ListenUpQuizGenerator implements QuizGenerator<ListenUpQuizDto> {

    @Override
    public ListenUpQuizDto generate(Graph graph) {
        // listenUpQuizCreate로직
        return null;
    }
}
