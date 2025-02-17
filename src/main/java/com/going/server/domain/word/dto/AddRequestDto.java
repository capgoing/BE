package com.going.server.domain.word.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddRequestDto {
    private Long clusterId;
    private String word;
}
