package com.going.server.global.common;

import lombok.Getter;


import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

