package com.going.server.domain.sentence.entity;

import com.going.server.domain.word.entity.Word;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@Node("Sentence")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sentence {

    @Id
    @GeneratedValue
    private Long sentenceId;

    @Property("sentence")
    private String sentence;
    // 문장은 단어를 사용한다.
    @Relationship(type = "USES", direction = Relationship.Direction.OUTGOING)
    private Word word;

    public static Sentence toEntity(String sentence, Word word) {
        return Sentence.builder()
                .sentence(sentence)
                .word(word)
                .build();
    }
}
