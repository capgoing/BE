package com.going.server.domain.quiz.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.Set;

@Builder
@Getter
public class PictureQuizDto {
    private final String imageUrl;
    private final Set<String> shuffled;
    private final String answer;
}
