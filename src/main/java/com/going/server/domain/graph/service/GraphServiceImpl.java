package com.going.server.domain.graph.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.going.server.domain.graph.dto.*;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.exception.NodeNotFoundException;
import com.going.server.domain.graph.repository.GraphEdgeRepository;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.graph.repository.GraphRepository;
import com.going.server.domain.upload.service.UploadServiceImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GraphServiceImpl implements GraphService {

    final GraphRepository graphRepository;
    final GraphNodeRepository graphNodeRepository;
    private final UploadServiceImpl uploadServiceImpl;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${unsplash.access-key}")
    private String unsplashKey;

    @Override
    public GraphListDto getGraphList() {
        List<Graph> graphs = graphRepository.findAll();

        List<GraphDto> graphDtos = new ArrayList<>();

        for (Graph graph : graphs) {
            Optional<GraphNode> targetNode = graph.getNodes().stream()
                    .filter(node -> node.getLevel() == 0)
                    .findFirst();
            String image = targetNode
                    .map(node -> getImage(node.getLabel()))
                    .orElse(null);
            GraphDto graphDto = GraphDto.of(graph, image, graph.isListenUpPerfect(), graph.isConnectPerfect(),graph.isPicturePerfect());
            graphDtos.add(graphDto);
        }
        return GraphListDto.of(graphDtos);
    }


    @Override
    public void deleteGraph(Long graphId) {
        Long dbId = graphRepository.findDbIdByGraphId(Long.valueOf(graphId));
        Graph graph = graphRepository.getByGraph(dbId);

        //그래프에 연결된 노드 삭제
        if (graph.getNodes() != null) {
            graph.getNodes().forEach(node -> graphNodeRepository.deleteById(node.getId()));
        }
        graphRepository.deleteById(dbId);
    }

    @Transactional(readOnly = true)
    @Override
    public KnowledgeGraphDto getGraph(Long graphId) {
        Long dbId = graphRepository.findDbIdByGraphId(graphId);
        Graph graph = graphRepository.getByGraph(dbId);
        log.info("[getGraph] 조회된 Graph ID: {}, Title: {}", graph.getId(), graph.getTitle());

        List<NodeDto> nodeDtoList = new ArrayList<>();
        List<EdgeDto> edgeDtoList = new ArrayList<>();

        List<GraphNode> nodes = graph.getNodes();
        if (nodes == null || nodes.isEmpty()) {
            log.warn("[getGraph] 해당 그래프에는 노드가 없습니다.");
            return KnowledgeGraphDto.of(nodeDtoList, edgeDtoList);
        }

        for (GraphNode node : nodes) {
            if (node == null) continue;

            log.debug("[getGraph] 노드 추가 - ID: {}, Label: {}", node.getNodeId(), node.getLabel());
            nodeDtoList.add(NodeDto.from(node));

            Set<GraphEdge> edges = node.getEdges();
            if (edges == null || edges.isEmpty()) continue;

            for (GraphEdge edge : edges) {
                if (edge == null || edge.getTarget() == null) continue;

                String sourceId = edge.getSource();
                String targetId = edge.getTarget().getNodeId().toString();
                String label = edge.getLabel();

                log.debug("[getGraph] 엣지 추가 - Source: {}, Target: {}, Label: {}", sourceId, targetId, label);
                edgeDtoList.add(EdgeDto.from(sourceId, targetId, label));
            }
        }

        return KnowledgeGraphDto.of(nodeDtoList, edgeDtoList);
    }


    @Override
    public NodeDto getNode(Long graphId, Long nodeId) {
        Long dbId = graphRepository.findDbIdByGraphId(graphId);
        Graph graph = graphRepository.getByGraph(dbId);

        //노드 찾기
        GraphNode node = null;
        for (GraphNode n : graph.getNodes()) {
            if (n.getNodeId().equals(nodeId)) {
                node = n;
                break;
            }
        }
        if (node == null) {
            throw new NodeNotFoundException(); // 직접 만든 예외 던지기
        }
        node.setImage(getImage(node.getLabel()));
        graphNodeRepository.save(node);
        return NodeDto.from(node);
    }

    @Override
    @Transactional
    public void addNode(Long graphId, NodeAddDto nodeAddDto) {
        Long dbId = graphRepository.findDbIdByGraphId(graphId);
        Graph graph = graphRepository.getByGraph(dbId);

        GraphNode parentNode = null;
        for (GraphNode node : graph.getNodes()) {
            if (node.getNodeId().equals(Long.parseLong(nodeAddDto.getParentId()))) {
                parentNode = node;
                break;
            }
        }
        if (parentNode == null) {
            throw new NodeNotFoundException(); // 직접 만든 예외 던지기
        }
        //graphNode parentNode = graphNodeRepository.getByNode(Long.parseLong(nodeAddDto.getParentId()));
        Long group = Long.parseLong(parentNode.getGroup()) + 1;
        Long newNodeId = graphNodeRepository.findMaxNodeId() + 1;

        GraphNode nodeEntity = GraphNode.builder()
                .nodeId(newNodeId)
                .label(nodeAddDto.getNodeLabel())
                .group(group.toString())
                .level(parentNode.getLevel() + 1)
                .includeSentence(null)
                .image(getImage(nodeAddDto.getNodeLabel()))
                .build();
        GraphNode newNode = graphNodeRepository.save(nodeEntity);

        GraphEdge newEdge = GraphEdge.builder()
                .source(parentNode.getNodeId().toString())
                .label(nodeAddDto.getEdgeLabel())
                .target(newNode)
                .build();
        if (parentNode.getEdges() == null) {
            parentNode.setEdges(new HashSet<>());
        }
        parentNode.getEdges().add(newEdge);
        graphNodeRepository.save(parentNode);

        graph = graphRepository.getByGraph(dbId);

        if (graph.getNodes() == null) {
            graph.setNodes(new ArrayList<>());
        }
        graph.getNodes().add(newNode);
        graphRepository.save(graph);
    }


    @Override
    public void deleteNode(Long graphId, Long nodeId) {
        //그래프 검증
        Long dbId = graphRepository.findDbIdByGraphId(graphId);
        Graph graph = graphRepository.getByGraph(dbId);
        GraphNode node = null;
        for (GraphNode n : graph.getNodes()) {
            if (n.getNodeId().equals(nodeId)) {
                node = n;
                break;
            }
        }
        if (node == null) {
            throw new NodeNotFoundException(); // 직접 만든 예외 던지기
        }
        //노드 삭제
        graphNodeRepository.deleteById(node.getId());
    }

    @Override
    public void modifyNode(Long graphId, Long nodeId, NodeModifyDto nodeModifyDto) {
        //그래프 검증
        Long dbId = graphRepository.findDbIdByGraphId(graphId);
        Graph graph = graphRepository.getByGraph(dbId);
        //노드 찾기
        GraphNode node = null;
        for (GraphNode n : graph.getNodes()) {
            if (n.getNodeId().equals(nodeId)) {
                node = n;
                break;
            }
        }
        if (node == null) {
            throw new NodeNotFoundException(); // 직접 만든 예외 던지기
        }
        //변경사항 수정
        node.setLabel(nodeModifyDto.getLabel());
        node.setIncludeSentence(nodeModifyDto.getIncludeSentence());
        //저장
        graphNodeRepository.save(node);
    }

    public String getImage(String keyword) {
        String translateKeyword = translate(keyword);
        try {
            String url = "https://api.unsplash.com/search/photos?query=" + translateKeyword + "&per_page=1&client_id=" + unsplashKey;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
            if (results != null && !results.isEmpty()) {
                Map<String, String> urls = (Map<String, String>) results.get(0).get("urls");
                return urls.get("regular");
            }
        } catch (Exception e) {
            System.out.println("이미지 검색 실패: " + keyword);
        }
        return null;
    }

    public String translate(String text) {
        try {
            String urlStr = String.format(
                    "https://translate.googleapis.com/translate_a/single?client=gtx&sl=ko&tl=en&dt=t&q=%s",
                    URLEncoder.encode(text, "UTF-8")
            );
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JsonArray json = JsonParser.parseReader(br).getAsJsonArray();
                return json.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsString();
            }
        } catch (Exception e) {
            System.out.println("번역 실패: " + text);
            return text;
        }
    }
}
