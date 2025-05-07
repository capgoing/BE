package com.going.server.domain.graph.dto;

import com.going.server.domain.graph.entity.GraphNode;
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
    private String includeSentence; //노드 확대 시 나올 설명
    private String group;

    public static NodeDto from(GraphNode node) {
        return NodeDto.builder().id(node.getNodeId().toString()).label(node.getLabel()).level(node.getLevel()).image(node.getImage()).includeSentence(node.getIncludeSentence()).group(node.getGroup()).build();
    }
}
