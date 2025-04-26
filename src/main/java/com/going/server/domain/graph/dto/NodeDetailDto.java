package com.going.server.domain.graph.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NodeDetailDto {
    private String id;
    private String word;
    private String sentence;
    private String image_url;
    private String audio_url;

    public static NodeDetailDto from(String id, String word, String sentence, String image_url, String audio_url) {
        return NodeDetailDto.builder().id(id).word(word).image_url(image_url).audio_url(audio_url).build();
    }
}
