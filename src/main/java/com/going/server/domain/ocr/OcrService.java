package com.going.server.domain.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrService {
    public String processOcr(MultipartFile file, String apiUrl, String secretKey) throws IOException {
        String jsonMessage = createJsonMessage(file);

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-OCR-SECRET", secretKey);
        connection.setReadTimeout(30000);

        // JSON 본문 전송
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.writeBytes(jsonMessage);
            outputStream.flush();
        }

        //응답 처리
        int responseCode = connection.getResponseCode();
        log.info("responseCode info = {}",responseCode);
        log.info("responseMessage info = {} ",connection.getResponseMessage());

        if (responseCode != HttpURLConnection.HTTP_OK) {
            try (var errorStream = connection.getErrorStream()) {
                if (errorStream != null) {
                    String errorBody = new String(errorStream.readAllBytes());
                    System.out.println("errorBody = " + errorBody);
                    throw new IOException("OCR 요청 실패: 상태 코드 = " + responseCode + ", 응답 = " + errorBody);
                }
            }

            throw new IOException("OCR 요청 실패: 상태 코드 = " + responseCode + connection.getResponseMessage());
        }

        return new String(connection.getInputStream().readAllBytes());
    }


    //OCR 요청에 필요한 JSON 메시지 생성 메소드
    private String createJsonMessage(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode root = objectMapper.createObjectNode();
        root.put("version", "V2");
        root.put("requestId", UUID.randomUUID().toString());
        root.put("timestamp", System.currentTimeMillis());

        ArrayNode images = objectMapper.createArrayNode();
        ObjectNode image = objectMapper.createObjectNode();

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        image.put("format", extension);
        image.put("name", "uploaded_file");
        //Base64 인코딩하여 data 필드로 추가
        String base64Data = java.util.Base64.getEncoder().encodeToString(file.getBytes());
        image.put("data", base64Data);

        images.add(image);
        root.set("images", images);

        return objectMapper.writeValueAsString(root);
    }
}
