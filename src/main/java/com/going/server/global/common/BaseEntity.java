package com.going.server.global.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {
    @CreatedDate
    @Field(name = "created_at", targetType = FieldType.TIMESTAMP)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Field(name = "updated_at", targetType = FieldType.TIMESTAMP)
    private LocalDateTime updateAt;
}

