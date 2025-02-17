package com.going.server.domain.word.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyRequestDto {
    private String word;

    public static ModifyRequestDto of(final String word) {
        return ModifyRequestDto.builder().word(word).build();
    }
}
