package com.going.server.domain.upload.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.ocr.OcrService;
import com.going.server.domain.ocr.PdfOcrService;
import com.going.server.domain.upload.dto.UploadRequestDto;
import com.going.server.domain.upload.dto.UploadResponseDto;

import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Session;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
public class UploadServiceImpl implements UploadService {
    private final OcrService ocrService;
    private final PdfOcrService pdfOcrService;
    private final GraphNodeRepository graphNodeRepository;
    private final GraphRepository graphRepository;
    private final Driver neo4jDriver; // Neo4j Java Driver

    @Value("${ocr.api.url}")
    private String apiUrl;
    @Value("${ocr.api.secret-key}")
    private String secretKey;
    @Value("${fastapi.base-url}")
    private String fastApiUrl;


    private final Map<String, String> translationCache = new HashMap<>();
    private final Map<String, String> imageCache = new HashMap<>();

    @Override
    public UploadResponseDto uploadFile(UploadRequestDto dto) {
        try {

            String jsonResponse = ocrService.processOcr(dto.getFile(), apiUrl, secretKey);
            log.info("jsonResponse log={}",jsonResponse);
            Map<String, String> paresData = pdfOcrService.parse(jsonResponse);
            String text = paresData.get("읽기자료");
            log.info("text log={}",text);
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
                String relationType = edgeNode.get("label").asText();

                GraphNode sourceNode = nodeIdToNode.get(sourceId);
                GraphNode targetNode = nodeIdToNode.get(targetId);

                if (sourceNode == null || targetNode == null) {
                    System.out.println("노드 매칭 실패: source=" + sourceId + ", target=" + targetId);
                    continue;
                }

                String dynamicCypher = String.format(
                        "MATCH (a:GraphNode {nodeId: '%s'}), (b:GraphNode {nodeId: '%s'}) MERGE (a)-[:`%s`]->(b)",
                        sourceId, targetId, relationType // 한글도 가능: "기능", "포함" 등
                );

                // session 통해 직접 실행
                try (Session session = neo4jDriver.session()) {
                    session.run(dynamicCypher);
                }

                //edge 엔티티 생성
                GraphEdge edge = GraphEdge.builder()
                        .source(sourceId)
                        .label(relationType)
                        .target(targetNode)
                        .build();

                // sourceNode에 edge 연결
                if (sourceNode.getEdges() == null) {
                    sourceNode.setEdges(new HashSet<>());
                }

                if (!sourceNode.getEdges().contains(edge)) {
                    sourceNode.getEdges().add(edge);
                }
                sourceNode.getEdges().add(edge);
            }

            //edge db에 저장
            graphNodeRepository.saveAll(nodeList);

            //그래프 생성
            String title = dto.getTitle();
            Long nextGraphId = graphRepository.findMaxGraphId();
            Graph graphEntity = Graph.builder()
                    .id(nextGraphId == null ? 1L : nextGraphId + 1) //id 직접 세팅
                    .title(title)
                    .content(text)
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

    // 모델 코드 호출
    public String setModelData(String text) {
        WebClient webClient = WebClient.builder().baseUrl(fastApiUrl).build();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        return webClient.post()
                .uri("/api/generate-gdb")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
