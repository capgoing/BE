package com.going.server.domain.graph.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.going.server.domain.graph.dto.*;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
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

    public GraphServiceImpl(GraphRepository graphRepository, GraphNodeRepository graphNodeRepository) {
        this.graphRepository = graphRepository;
        this.graphNodeRepository = graphNodeRepository;
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
    public KnowledgeGraphDto addNode(Long graphId, NodeAddDto nodeAddDto) {
        //graphId로 그래프 찾기
        graphRepository.getByGraph(graphId);

        //부모노드id로 부모노드 찾기
        GraphNode parentNode = graphNodeRepository.getByNode(Long.parseLong(nodeAddDto.getParentId()));
        Long group =Long.parseLong(parentNode.getGroup())+1;
        Long newNodeId = graphNodeRepository.findMaxNodeId() + 1;

        //새로운 Node 엔티티 생성
        GraphNode nodeEntity = GraphNode.builder()
                .nodeId(newNodeId)
                .label(nodeAddDto.getNodeLabel())
                .group(group.toString())
                .level(parentNode.getLevel()+1)
                .includeSentence(nodeAddDto.getIncludeSentence())
                .build();
        //DB에 노드 저장
        GraphNode newNode = graphNodeRepository.save(nodeEntity);

        //Source: 연결선의 출발점 -> 부모노드 id
        //Target: 연결선의 도착점 -> 새로 추가한 노드 id
        //edge 연결
        GraphEdge newEdge = GraphEdge.builder()
                .source(parentNode.getNodeId().toString())
                .label(nodeAddDto.getEdgeLabel())
                .target(newNode)
                .build();
        if(parentNode.getEdges() == null) {
            parentNode.setEdges(new HashSet<>());
        }
        parentNode.getEdges().add(newEdge);
        graphNodeRepository.save(parentNode);

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
        //새 노드,엣지 추가
        NodeDto newNodeDto = NodeDto.from(newNode, null);
        EdgeDto newEdgeDto = EdgeDto.from(
                newEdge.getSource(),
                newEdge.getTarget().getNodeId().toString(),
                newEdge.getLabel()
        );
        nodeDtoList.add(newNodeDto);
        edgeDtoList.add(newEdgeDto);

        return KnowledgeGraphDto.of(nodeDtoList,edgeDtoList);
    }
}
