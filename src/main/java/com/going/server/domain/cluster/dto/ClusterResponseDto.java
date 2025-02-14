package com.going.server.domain.cluster.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterResponseDto {
    private Long cluster_id;
    private String representName;
}
