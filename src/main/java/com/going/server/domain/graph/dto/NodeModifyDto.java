package com.going.server.domain.graph.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeModifyDto {
    @NotBlank(message = "수정할 노드의 이름(label)을 입력해주세요.")
    private String label;
    @NotBlank(message = "수정할 노드의 설명(includeSentence)을 입력해주세요.")
    private String includeSentence;
}
