package com.going.server.domain.word.dto;

import com.going.server.domain.word.entity.Word;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordDto {
    private String compositionWord;
    private Boolean isRepresent;
    public static WordDto from(String compositionWord, Boolean isRepresent) {
        return WordDto.builder().compositionWord(compositionWord).isRepresent(isRepresent).build();
    }
}
