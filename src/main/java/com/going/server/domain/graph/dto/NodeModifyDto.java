package com.going.server.domain.graph.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeModifyDto {
    @NotNull(message = "수정할 노드의 이름(label)을 입력해주세요.")
    private String label;
    @NotNull(message = "수정할 노드의 설명(includeSentence)을 입력해주세요.")
    private String includeSentence;
}
