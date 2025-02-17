package com.going.server.domain.word.dto;

import com.going.server.domain.word.entity.Word;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordResponseDto {
    private Long clusterId;
    private List<WordDto> words;
    public static WordResponseDto of(Long clusterId, List<WordDto> words) {
        return WordResponseDto.builder().clusterId(clusterId).words(words).build();
    }
}
