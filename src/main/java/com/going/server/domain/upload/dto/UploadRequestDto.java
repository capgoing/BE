package com.going.server.domain.upload.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadRequestDto {
    @NotNull(message = "파일이 필요합니다.")
    private MultipartFile file;
}
