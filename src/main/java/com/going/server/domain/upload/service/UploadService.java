package com.going.server.domain.upload.service;

import com.going.server.domain.upload.dto.UploadRequestDto;
import com.going.server.domain.upload.dto.UploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public interface UploadService {

    UploadResponseDto uploadFile(UploadRequestDto dto);
}
