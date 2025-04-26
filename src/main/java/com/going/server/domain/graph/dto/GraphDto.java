package com.going.server.domain.graph.dto;

import com.going.server.domain.graph.entity.Graph;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GraphDto {
    private Long id;
    private String title;
    private String image;
    private Boolean easy;
    private Boolean hard;
    public static GraphDto of(Graph graph, String image,Boolean easy, Boolean hard) {
        return GraphDto.builder().id(graph.getId()).title(graph.getTitle()).image(image).easy(easy).hard(hard).build();
    }
}
