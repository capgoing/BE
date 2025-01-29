package com.going.server.domain.word.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompositionClusterResponseDto {
    private Integer clusterId; // 클러스터 ID
    private List<CompositionWordResponseDto> words; // 해당 클러스터의 단어 목록
}
