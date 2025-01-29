package com.going.server.domain.word.entity;

import com.going.server.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.bson.types.ObjectId;

@Document(collection = "words") // MongoDB 컬렉션 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word extends BaseEntity {

    @Id
    private String id;  // MongoDB에서 자동 생성될 ObjectId를 String으로 저장

    @Field(name = "word")
    private String word;

    // ID를 자동으로 생성하는 생성자 추가
    public Word(String word) {
        this.id = new ObjectId().toString();  // MongoDB의 ObjectId를 자동으로 생성
        this.word = word;
    }

}
