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
    private Long id;

//    @Property("word_id")
//    private Long wordId;

    @Property("compose_word")
    private String composeWord;

    // Neo4j 관계 설정
    // 하나의 단어는 해당 클러스터에 속한다. Word -> Cluster 라는 의미
    // BELONGS_TO 는 속한다는 관계를 나타내고, OUTGOING은 방향성을 나타냄
    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Cluster cluster;

    public static Word toEntity(String composeWord, Cluster cluster) {
        return Word.builder()
                .composeWord(composeWord)
                .cluster(cluster)
                .build();
    }
}

