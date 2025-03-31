package com.going.server.domain.word.entity;

import com.going.server.domain.cluster.entity.Cluster;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@Node("Word")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Word {

    @Id
    @GeneratedValue
    private Long wordId;

    @Property("compose_word")
    private String composeWord;

    // Neo4j 관계 설정
    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Cluster cluster;

    public static Word toEntity(String composeWord, Cluster cluster) {
        return Word.builder()
                .composeWord(composeWord)
                .cluster(cluster)
                .build();
    }
}

