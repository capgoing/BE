package com.going.server.domain.cluster.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterResponseDto {
    private List<ClusterDto> cluster;
    private String imageUrl;
    public static ClusterResponseDto from(List<ClusterDto> cluster, String imageUrl) {
        return ClusterResponseDto.builder().imageUrl(imageUrl).cluster(cluster).build();
    }
}
