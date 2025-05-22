package com.going.server.domain.ocr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PdfOcrService implements OcrParser {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Map<String, String> parse(String response) {
        Map<String, String> result = new HashMap<>();
        try{
            JsonNode root = objectMapper.readTree(response);

            //첫번째 파일에서 데이터 추출
            JsonNode file = root.path("images").get(0);
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("OCR 응답에서 이미지 데이터가 없습니다.");
            }

            JsonNode titleNode = file.path("title");
            if (!titleNode.isMissingNode()) {
                String name = titleNode.path("name").asText();
                String value = titleNode.path("inferText").asText();
                result.put(name, value);
            }
//            JsonNode fields = file.path("fields");
//            for (JsonNode field : fields) {
//                String name = field.path("name").asText();
//                String value = field.path("inferText").asText();
//                result.put(name, value);
//            }
        }catch (Exception e){
            log.error("OCR 응답 파싱 중 오류 발생",e);
            throw new RuntimeException("OCR 응답 파싱 실패",e);
        }
        return result;
    }
}
