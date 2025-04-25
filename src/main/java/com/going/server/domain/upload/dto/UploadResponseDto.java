package com.going.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadResponseDto {
    private Long graphId;

    public static UploadResponseDto from(Long graphId) {
        return UploadResponseDto.builder().graphId(graphId).build();
    }
}
