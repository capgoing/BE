package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.quiz.dto.ConnectQuizDto;
import org.springframework.stereotype.Component;

@Component
public class ConnectQuizGenerator implements QuizGenerator<ConnectQuizDto> {

    @Override
    public ConnectQuizDto generate(Graph graph) {
        //  createConnectQuizDto 로직
        return  null;
    }
}
