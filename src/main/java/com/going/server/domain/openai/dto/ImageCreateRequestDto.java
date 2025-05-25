package com.going.server.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageCreateRequestDto {
    private String prompt;
    private String model = "dall-e-3";
    private String size = "1024x1024"; // 기본값으로 지정
    private int n = 1; // 기본값 넣어두기
}