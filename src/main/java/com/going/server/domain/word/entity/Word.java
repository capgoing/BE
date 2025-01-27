package com.going.server.domain.word.entity;

import com.going.server.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "words")
public class Word extends BaseEntity {
    @Id
    private String id;

    @Field(name = "word")
    private String word;
}
