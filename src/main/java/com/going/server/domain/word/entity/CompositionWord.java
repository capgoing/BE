package com.going.server.domain.word.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "composition_words")
public class CompositionWord {
    @Id
    private String compositionId;

    @Field(name = "composition_word")
    private String compositionWord;

    @Field(name = "is_represent")
    private Boolean isRepresent;

    // ID를 자동으로 생성하는 생성자 추가
    public CompositionWord(String word) {
        this.compositionId = new ObjectId().toString();  // MongoDB의 ObjectId를 자동으로 생성
        this.compositionWord = word;
        this.isRepresent = false;
    }
}
