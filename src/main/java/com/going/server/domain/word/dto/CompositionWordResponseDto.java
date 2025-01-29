package com.going.server.domain.word.dto;

import com.going.server.domain.word.entity.CompositionWord;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompositionWordResponseDto {
    private String compositionWord;
    private Boolean isRepresent;

    public static CompositionWordResponseDto of(CompositionWord compositionWord) {
        return new CompositionWordResponseDto(
                compositionWord.getCompositionWord(),
                compositionWord.getIsRepresent()
        );
    }
}
