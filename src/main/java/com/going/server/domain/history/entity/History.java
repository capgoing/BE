package com.going.server.domain.history.entity;

import com.going.server.domain.word.entity.Word;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@Node("History")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

    @Id
    @GeneratedValue
    private Long historyId;

    @Property("before")
    private String before;

    @Property("after")
    private String after;

    @Relationship(type = "RELATED_TO", direction = Relationship.Direction.OUTGOING)
    private Word word;

    public static History toEntity(String before, String after, Word word){
        return History.builder()
                .before(before)
                .after(after)
                .word(word)
                .build();
    }
}
