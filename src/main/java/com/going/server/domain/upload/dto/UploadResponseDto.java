package com.going.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadResponseDto {
    private Long graphId;
    private String title;
    private Boolean easy; //쉬움 모드
    private Boolean hard; //어려움 모드
    private Long nodes; //그래프 노드 개수
    private Long edges; //그래프 관계 개수

    public static UploadResponseDto from(Long graphId, String title, Boolean easy, Boolean haed, Long nodes, Long edges) {
        return UploadResponseDto.builder().graphId(graphId).title(title).easy(easy).hard(haed).nodes(nodes).edges(edges).build();
    }
}
