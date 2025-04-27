package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import org.springframework.stereotype.Component;

@Component
public class PictureQuizGenerator implements QuizGenerator<PictureQuizDto> {

    @Override
    public PictureQuizDto generate(Graph graph) {
        // TODO: picture 퀴즈 생성 로직

        return null;
    }
}
