package com.going.server.domain.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageCreateResponseDto {
    private List<Data> data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String url;
        private String revised_prompt;
    }
}