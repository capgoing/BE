package com.going.server.domain.graph.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeAddDto {
    @NotNull(message = "추가할 부모 노드의 ID를 입력해주세요.")
    String parentId;
    @NotNull(message = "추가할 라벨(nodeLabel)을 입력해주세요.")
    String nodeLabel;
    @NotNull(message = "추가할 노드의 관계(edgeLabel)를 입력해주세요.")
    String edgeLabel;
    @NotNull(message = "문장을 입력해주세요")
    String includeSentence;
}
