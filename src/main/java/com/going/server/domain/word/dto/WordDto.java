package com.going.server.domain.word.dto;

import com.going.server.domain.word.entity.Word;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordDto {
    private Long wordId;
    private String compositionWord;
    private Boolean isRepresent;
    public static WordDto from(Long wordId, String compositionWord, Boolean isRepresent) {
        return WordDto.builder().wordId(wordId).compositionWord(compositionWord).isRepresent(isRepresent).build();
    }
}
