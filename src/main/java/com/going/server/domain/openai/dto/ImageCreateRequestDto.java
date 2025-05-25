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
    private String model = "dall-e-3";
    private String style = "vivid";
    private String size = "1024x1024";
    private int n = 1;

    public ImageCreateRequestDto(String prompt) {
        this.prompt = prompt;
    }
}
