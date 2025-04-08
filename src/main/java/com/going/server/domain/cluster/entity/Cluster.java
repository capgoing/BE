package com.going.server.domain.cluster.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;


@Node("Cluster")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cluster {
    @Id
    @GeneratedValue
    private Long id;

    @Property("cluster_id")
    private Long clusterId;

    @Property("represent_word")
    private String representWord;

    @Property("result_img")
    private String resultImg;

    public static Cluster toEntity(String representWord, String resultImg) {
        return Cluster.builder()
                .representWord(representWord)
                .resultImg(resultImg)
                .build();
    }
}
