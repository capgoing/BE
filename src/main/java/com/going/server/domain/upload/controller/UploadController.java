package com.going.server.domain.upload.controller;

import com.going.server.domain.upload.dto.UploadRequestDto;
import com.going.server.domain.upload.dto.UploadResponseDto;
import com.going.server.domain.upload.service.UploadService;
import com.going.server.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Tag(name = "[캡스톤]Upload", description = "파일 업로드 및 지식그래프 생성을 위한 API")
public class UploadController {
    private final UploadService uploadService;
    //파일 업로드
    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "PDF 업로드 및 그래프 생성", description = "PDF 파일을 업로드 하고, 텍스트를 추출한 후 지식 그래프를 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OCR 후 지식그래프 결과를 성공적으로 반환했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\":\"\"}")
                    )
            )
    })
    public SuccessResponse<UploadResponseDto> uploadFile(@Valid @ModelAttribute UploadRequestDto dto) {
        UploadResponseDto responseDto = uploadService.uploadFile(dto);
        return SuccessResponse.of(responseDto);
    }
}
