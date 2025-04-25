package com.going.server.domain.upload.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.going.server.domain.graph.dto.edgeDto;
import com.going.server.domain.graph.dto.nodeDto;
import com.going.server.domain.graph.entity.Edge;
import com.going.server.domain.graph.repository.EdgeRepository;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements  UploadService {
    private final OcrService ocrService;
    private final PdfOcrService pdfOcrService;
    private final EdgeRepository edgeRepository;
    @Value("${ocr.api.url}")
    private String apiUrl;
    @Value("${ocr.api.secret-key}")
    private String secretKey;

    @Override
    public UploadResponseDto uploadFile(UploadRequestDto dto) {
        try {
            String jsonResponse = ocrService.processOcr(dto.getFile(), apiUrl, secretKey);
            Map<String, String> paresData = pdfOcrService.parse(jsonResponse);
            String text = paresData.get("6학년 읽기자료 내용");
            System.out.println("추출된 텍스트: " + text);

            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
            JsonNode root = mapper.readTree(is);
            JsonNode dataNode = root.get("data");
            JsonNode edgesNode = dataNode.get("edges");

            List<Edge> edgeList = new ArrayList<>();

            for (JsonNode edgeNode : edgesNode) {
                if (!edgeNode.has("source") || !edgeNode.has("target") || !edgeNode.has("label")) {
                    System.out.println("필드 누락: " + edgeNode.toPrettyString());
                    continue;
                }

                String source = edgeNode.get("source").asText();
                String target = edgeNode.get("target").asText();
                String label = edgeNode.get("label").asText();

                Edge edge = Edge.builder()
                        .source(source)
                        .target(target)
                        .label(label)
                        .build();

                edgeList.add(edge);
            }

            edgeRepository.saveAll(edgeList);
            System.out.println("총 " + edgeList.size() + "개의 edge가 저장되었습니다.");

            return UploadResponseDto.from(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
