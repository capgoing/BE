package com.going.server.domain.word.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordResponseDto {
    private Integer clusterId;
    private String word;
    private String result_img;
}
