package com.going.server.domain.word.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordResponseDto {
    private Integer clusterId; // 클러스터 ID 추가
    private String word;
    private String result_img;
}
