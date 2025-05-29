package com.going.server.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageCreateRequestDto {
    private String prompt;
    private String model;
    private String style;
    private String quality;
    private String size;
    private int n = 1;
}
