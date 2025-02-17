package com.going.server.domain.cluster.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterDto {
    private Long clusterId;
    private String representWord;
    public static ClusterDto from(Long clusterId, String representWord) {
        return ClusterDto.builder().clusterId(clusterId).representWord(representWord).build();
    }
}
