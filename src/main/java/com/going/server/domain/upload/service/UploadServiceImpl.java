package com.going.server.domain.upload.service;

import com.going.server.domain.ocr.OcrService;
import com.going.server.domain.ocr.PdfOcrService;
import com.going.server.domain.upload.dto.UploadRequestDto;
import com.going.server.domain.upload.dto.UploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements  UploadService {
    private final OcrService ocrService;
    private final PdfOcrService pdfOcrService;
    @Value("${ocr.api.url}")
    private String apiUrl;
    @Value("${ocr.api.secret-key}")
    private String secretKey;

    @Override
    public UploadResponseDto uploadFile(UploadRequestDto dto) {
        try {
            String jsonResponse = ocrService.processOcr(dto.getFile(),apiUrl,secretKey);
            //System.out.println("jsonResponse: "+jsonResponse);
            Map<String, String> paresData = pdfOcrService.parse(jsonResponse);
            //System.out.println("paresData: "+paresData);
            
            String text = paresData.get("6학년 읽기자료 내용");
            System.out.println("추출된 텍스트: "+text);

            //TODO : 그래프 생성을 위한 FastApi 호출 코드 작성

            //TODO : 데이터 받아와서 DB에 저장

            return UploadResponseDto.from(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }  catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
