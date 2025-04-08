package com.going.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResponseDto {
    private String text;

    @Builder
    public UploadResponseDto(String text) {
        this.text = text;
    }
}
