package com.going.server.domain.cluster.dto;

import com.going.server.domain.word.entity.Word;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterDto {
    private Long clusterId;
    private String representWord;
    private List<String> words;
    public static ClusterDto from(Long clusterId, String representWord, List<String> words) {
        return ClusterDto.builder().clusterId(clusterId).representWord(representWord).words(words).build();
    }
}
