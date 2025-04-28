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
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class GraphServiceImpl implements GraphService {

    private final GraphRepository graphRepository;
    private final GraphNodeRepository graphNodeRepository;
    private final GraphEdgeRepository graphEdgeRepository;

    public GraphServiceImpl(GraphRepository graphRepository, GraphNodeRepository graphNodeRepository, GraphEdgeRepository graphEdgeRepository) {
        this.graphRepository = graphRepository;
        this.graphNodeRepository = graphNodeRepository;
        this.graphEdgeRepository = graphEdgeRepository;
    }

    @Override
    public GraphListDto getGraphList() {
        List<Graph> graphs = graphRepository.findAll();
        List<GraphDto> graphDtos = new ArrayList<>();
        for (Graph graph : graphs) {
            GraphDto graphDto = GraphDto.of(graph,null,false,false);
            graphDtos.add(graphDto);
        }
        return GraphListDto.of(graphDtos);
    }

    @Override
    public void deleteGraph(Long graphId) {
        Graph graph = graphRepository.getByGraph(graphId);
        graphRepository.deleteById(graph.getId());
    }

    @Override
    public KnowledgeGraphDto getGraph(Long graphId) {
        Graph graph = graphRepository.getByGraph(graphId);

        List<NodeDto> nodeDtoList = new ArrayList<>();
        List<EdgeDto> edgeDtoList = new ArrayList<>();

        for (GraphNode node : graph.getNodes()) {
            NodeDto nodeDto = NodeDto.from(node, null);
            nodeDtoList.add(nodeDto);

            if (node.getEdges() != null) {
                for (GraphEdge edge : node.getEdges()) {
                    EdgeDto edgeDto = EdgeDto.from(edge.getSource(),edge.getTarget().getNodeId().toString(),edge.getLabel());
                    edgeDtoList.add(edgeDto);
                }
            }
        }

        return KnowledgeGraphDto.of(nodeDtoList, edgeDtoList);
    }

    @Override
    public NodeDto getNode(Long graphId, Long nodeId) {
        graphRepository.getByGraph(graphId);
        GraphNode node = graphNodeRepository.getByNode(nodeId);
        return NodeDto.from(node,null);
    }

    @Override
    @Transactional
    public void addNode(Long graphId, NodeAddDto nodeAddDto) {
        Graph graph = graphRepository.getByGraph(graphId);
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
                .includeSentence(nodeAddDto.getIncludeSentence())
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

        graph = graphRepository.getByGraph(graphId);

        if (graph.getNodes() == null) {
            graph.setNodes(new ArrayList<>());
        }
        graph.getNodes().add(newNode);
        graphRepository.save(graph);
    }


    @Override
    public void deleteNode(Long graphId, Long nodeId) {
        //그래프 검증
        Graph graph = graphRepository.getByGraph(graphId);
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
        Graph graph = graphRepository.getByGraph(graphId);
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
}
