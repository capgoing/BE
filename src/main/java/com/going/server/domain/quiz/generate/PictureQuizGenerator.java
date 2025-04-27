package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.quiz.dto.ListenUpQuizDto;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PictureQuizGenerator implements QuizGenerator<PictureQuizDto> {

    @Override
    public PictureQuizDto generate(Graph graph) {
        Set<String> shuffled = new HashSet<>();
        String answer;

        return PictureQuizDto.builder()
                .imageUrl("https://s3-example")
                .shuffledOptions(shuffled)
                .answer(answer)
                .build();
    }
}
