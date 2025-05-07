package com.going.server.domain.upload.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphEdgeRepository;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.ocr.OcrService;
import com.going.server.domain.ocr.PdfOcrService;
import com.going.server.domain.upload.dto.UploadRequestDto;
import com.going.server.domain.upload.dto.UploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements  UploadService {
    private final OcrService ocrService;
    private final PdfOcrService pdfOcrService;
    private final GraphNodeRepository graphNodeRepository;
    private final GraphRepository graphRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ocr.api.url}")
    private String apiUrl;
    @Value("${ocr.api.secret-key}")
    private String secretKey;
    @Value("${unsplash.access-key}")
    private String unsplashKey;
    @Value("${fastapi.base-url}")
    private String fastApiUrl;

    @Override
    public UploadResponseDto uploadFile(UploadRequestDto dto) {
        try {
            String jsonResponse = ocrService.processOcr(dto.getFile(), apiUrl, secretKey);
            Map<String, String> paresData = pdfOcrService.parse(jsonResponse);
            String text = paresData.get("6학년 읽기자료 내용");
            //System.out.println("추출된 텍스트: " + text);

            //모델에 돌린 값을 받아옴
            String response = setModelData(text);

            ObjectMapper mapper = new ObjectMapper();
            //InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
            JsonNode root = mapper.readTree(response);
            JsonNode graph = root.get("data");
            JsonNode nodesNode = graph.get("nodes");
            JsonNode edgesNode = graph.get("edges");

            //node 데이터 파싱코드
            List<GraphNode> nodeList = new ArrayList<>();
            for (JsonNode node : nodesNode) {
                String label = node.get("label").asText();
                GraphNode graphNode = GraphNode.builder()
                        .nodeId(Long.parseLong(node.get("id").asText()))
                        .label(label)
                        .group(node.get("group").asText())
                        .level(node.get("level").asLong())
                        .includeSentence(node.get("includeSentence").asText())
                        .image(null)
                        .build();
                nodeList.add(graphNode);
            }
            //DB에 저장
            graphNodeRepository.saveAll(nodeList);

            //노드 ID로 빠르게 찾을 수 있게 Map 생성
            Map<String, GraphNode> nodeIdToNode = new HashMap<>();
            for (GraphNode graphNode : nodeList) {
                nodeIdToNode.put(String.valueOf(graphNode.getNodeId()), graphNode);
            }

            //edge 데이터 파싱 코드
            List<GraphEdge> edgeList = new ArrayList<>();
            for (JsonNode edgeNode : edgesNode) {
                if (!edgeNode.has("source") || !edgeNode.has("target") || !edgeNode.has("label")) {
                    System.out.println("필드 누락: " + edgeNode.toPrettyString());
                    continue;
                }
                String sourceId = edgeNode.get("source").asText();
                String targetId = edgeNode.get("target").asText();
                String label = edgeNode.get("label").asText();

                GraphNode sourceNode = nodeIdToNode.get(sourceId);
                GraphNode targetNode = nodeIdToNode.get(targetId);

                if (sourceNode == null || targetNode == null) {
                    System.out.println("노드 매칭 실패: source=" + sourceId + ", target=" + targetId);
                    continue;
                }

                //edge 엔티티 생성
                GraphEdge edge = GraphEdge.builder()
                        .source(sourceId)
                        .label(label)
                        .target(targetNode)
                        .build();

                // sourceNode에 edge 연결
                if (sourceNode.getEdges() == null) {
                    sourceNode.setEdges(new HashSet<>());
                }
                sourceNode.getEdges().add(edge);
            }

            //edge db에 저장
            graphNodeRepository.saveAll(nodeList);

            //그래프 생성
            String title = dto.getTitle();
            Graph graphEntity = Graph.builder()
                    .title(title)
                    .listenUpPerfect(false)
                    .connectPerfect(false)
                    .picturePerfect(false)
                    .nodes(nodeList)
                    .build();
            graphRepository.save(graphEntity);

//            // 양방향 연결 설정 (GraphNode → Graph)
//            for (GraphNode graphNode : nodeList) {
//                graphNode.setGraph(graphEntity);
//            }
//
//            // 저장 (이제 연결된 상태로 저장됨)
//            graphRepository.save(graphEntity);
//            graphNodeRepository.saveAll(nodeList);

            return UploadResponseDto.from(
                    graphEntity.getId()
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public String getImage(String keyword) {
        String url =  "https://api.unsplash.com/search/photos?query=" + keyword + "&per_page=1&client_id=" + unsplashKey;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
        if (results != null && !results.isEmpty()) {
            Map<String, Object> firstResult = results.get(0);
            Map<String, String> urls = (Map<String, String>) firstResult.get("urls");
            return urls.get("regular"); // 또는 "small", "thumb" 등
        }
        return null;
    }

    //번역 api 호출
    public String translate(String text) {
        String url = "https://libretranslate.com/translate";
        Map<String, Object> body = new HashMap<>();
        body.put("q",text);
        body.put("source","ko");
        body.put("target","en");
        body.put("format","text");

        //http 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
        System.out.println("번역결과: "+response.getBody().get("translatedText"));
        return (String) response.getBody().get("translatedText");
    }

    //FastApi 호출
    public String setModelData(String text) {
        WebClient webClient = WebClient.builder()
                .baseUrl(fastApiUrl)
                .build();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        String response = webClient.post()
                .uri("/api/generate-gdb")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //System.out.println("응답 결과: " + response);
        return response;
    }
}
