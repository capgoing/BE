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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
    private final GraphEdgeRepository graphEdgeRepository;
    private final GraphRepository graphRepository;
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
            JsonNode graph = root.get("data");
            JsonNode nodesNode = graph.get("nodes");
            JsonNode edgesNode = graph.get("edges");

            //node 데이터 파싱코드
            List<GraphNode> nodeList = new ArrayList<>();
            for (JsonNode node : nodesNode) {
                GraphNode graphNode = GraphNode.builder()
                        .nodeId(Long.parseLong(node.get("id").asText()))
                        .label(node.get("label").asText())
                        .group(node.get("group").asText())
                        .level(node.get("level").asLong())
                        .includeSentence(node.get("includeSentence").asText())
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
}
