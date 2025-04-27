package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;

public interface QuizGenerator<T> {
    T generate(Graph graph);
}
