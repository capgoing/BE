package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NodeDto {
    private String id;
    private String label; //노트에 나올 아름
    private Long level; //노드 깊이
    private String image; //노드 이미지 주소
    private String description; //노드 확대 시 나올 설명

    public static NodeDto from(String id, String label, Long level, String image, String description) {
        return NodeDto.builder().id(id).label(label).level(level).image(image).description(description).build();
    }
}
